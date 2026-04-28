# Inference-Related Annotations

## `@OnlyInputTypes`

A type parameter annotation.
Root issue [KT-13198](https://youtrack.jetbrains.com/issue/KT-13198).
Breaks liskov substitution principle [KT-58171](https://youtrack.jetbrains.com/issue/KT-58171).
Report incompatible comparisons issue [KT-57779](https://youtrack.jetbrains.com/issue/KT-57779).

### What It Does

Once all variables are fixed during constraint system completion,
checks that annotated type parameter equals to one of the input types:
- Receiver type
- Argument types
- Expected type
- Explicit type arguments
- Some others?

### What It Solves

In the standard library: avoid comparing unrelated types.

```kotlin
fun <@kotlin.internal.OnlyInputTypes T> Array<out T>.indexOf(element: T): Int
fun <@kotlin.internal.OnlyInputTypes T> Iterable<T>.contains(element: T): Boolean
fun <@kotlin.internal.OnlyInputTypes T> MutableCollection<out T>.remove(element: T): Boolean
```

---

In [mongodb driver](https://github.com/mongodb/mongo-java-driver/blob/f5e738398f54758fd4ce70d5b7127375bf48e241/driver-kotlin-extensions/src/main/kotlin/com/mongodb/kotlin/client/model/Updates.kt#L55):
avoid `KProperty` of unrelated type when setting/comparing values.

```kotlin
fun <@OnlyInputTypes T> set(property: KProperty<T?>, value: T?): Bson = property.set(value)
```

Should not `@Exact` be used instead here?
Otherwise, the value can be of a broader type than the property.

```kotlin
fun <T> set(property: KProperty<@Exact T?>, value: T?): Bson = property.set(value)
```

Scala libraries solve this by [demanding types to be equal](https://github.com/Tinkoff/oolong/blob/main/oolong-core/src/main/scala/ru/tinkoff/oolong/dsl/Dsl.scala#L28):

```scala
def set[PropT, ValueT](selectProp: DocT => PropT, value: ValueT)(using
  PropT =:= ValueT
): Updater[DocT]
```

Java just does not use reflection [here](https://github.com/mongodb/mongo-java-driver/blob/f5e738398f54758fd4ce70d5b7127375bf48e241/driver-core/src/main/com/mongodb/client/model/Updates.java#L89).

---

[Kotest](https://github.com/kotest/kotest/blob/9728b2b3147eb8986973d5cb2d8477a3c1e3aa7e/kotest-assertions/kotest-assertions-core/src/commonMain/kotlin/io/kotest/matchers/equals/shouldEqual.kt#L32):
equality, similar to the standard library.

---

[signum](https://github.com/a-sit-plus/signum/blob/ddbbc951d58771e239476edadcf9a60d0f00bde4/internals/src/commonMain/kotlin/at/asitplus/signum/internals/Utils.kt#L20):
implementing `as` with custom exception.

```kotlin
inline fun <@kotlin.internal.OnlyInputTypes O, reified T : O> checkedAs(v: O): T =
    v as? T ?: throw TODO()
```

Should not `@Exact` be used instead here?

```kotlin
inline fun <O, reified T : O> checkedAs(v: @Exact O): T
```

---

[kord-extensions](https://github.com/Kord-Extensions/kord-extensions/blob/f617fa264674fc2f566a46e85b43609f14d1ef4a/kord-extensions/src/main/kotlin/dev/kordex/core/utils/deltas/ChangeSet.kt#L26):
avoid `KProperty` of unrelated type.

```kotlin
fun <@kotlin.internal.OnlyInputTypes T> get(key: KProperty<T>): Change<T> =
    changes[key] as? Change<T>?
        ?: throw NoSuchElementException("No such element: $key")
```

Should not `@Exact` be used instead here?

## `@Exact`

A type (not parameter) annotation.
Expose `@Exact` [issue](https://youtrack.jetbrains.com/issue/KT-49194/Expose-kotlin.internal.Exact-or-some-other-mechanism-for-fixing-types.).
Necessity of `@Exact` in DSLs [issue](https://youtrack.jetbrains.com/issue/KT-79654/Receiver-bound-type-parameters).
`@Exact` on elvis [investigation](https://docs.google.com/document/d/10rzeYwbjM6odAOp6PObO4ft028lAFwDb2i49t3t7n6E/edit?tab=t.0).

### What It Does

Once marked type participates in any subtyping constraint `T <: U`,
the opposite constraint `U <: T` is added to the constraint system too,
effectively enforcing equality.

### What It Solves

In the stdlib: avoid upcasting a map when using it as a delegate.

```kotlin
fun <V, V1 : V> Map<in String, @Exact V>.getValue(thisRef: Any?, property: KProperty<*>): V1
```

---

Often in DSLs, but not limited to them:

Disable variance where it exists on the declaration of a generic type.

In the first two cases it is rather avoiding meaningless equality checks?

[kuick](https://github.com/gokoan/kuick/blob/d4d6e66783e09df6b43ac9e4ae926846bfb2ac7f/kuick-core/src/commonMain/kotlin/kuick/repositories/ModelQuery.kt#L96):

```kotlin
infix fun <T : Any, V : Any?> KProperty1<T, @Exact V>.ne(value: V)
```

[squash](https://github.com/orangy/squash/blob/dc025481b7104a5e234d6df370d715b6b37c81eb/squash-core/src/org/jetbrains/squash/expressions/ExpressionBuilder.kt#L14):

```kotlin
infix fun <V> Expression<@Exact V?>.eq(other: Expression<V?>): Expression<Boolean>
```

In the next two cases, I think the rough idea is the following:
we capture a "reference" to value of type `V` in some class which
itself is covariant in `V`, but we can use the reference to
do writes, so we want invariance for such usages.

[squash](https://github.com/orangy/squash/blob/dc025481b7104a5e234d6df370d715b6b37c81eb/squash-core/src/org/jetbrains/squash/statements/Update.kt#L13):

```kotlin
operator fun <V, S : V> set(column: Column<@Exact V>, value: Expression<S>)
```

[kotlin-jdsl](https://github.com/line/kotlin-jdsl/blob/8e4730e998a57fceddd2e620f016460fe3fd943a/query-model/jpql/src/main/kotlin/com/linecorp/kotlinjdsl/querymodel/jpql/path/Paths.kt#L24):

```kotlin
fun <T : Any, V> path(property: KProperty1<T, @Exact V>): Path<V & Any>
```

Disable subtyping.

[kotlin-null-defaults](https://github.com/kyay10/kotlin-null-defaults/blob/417801bee1b7f9eb44f6976fe5752f2853fe20a3/kotlin-plugin/src/main/kotlin/io/github/kyay10/kotlinnulldefaults/utils/StandardExtensions.kt#L22):

```kotlin
operator fun <T> @kotlin.internal.Exact T.plus(collection: Collection<T>): List<T>
```

Not sure if it is semantically really necessary here.

---

mirai strange [use-case](https://github.com/mamoe/mirai/blob/283f8840d4682cc30fbdd87c66fe76f6a71ff8db/mirai-console/backend/mirai-console/src/data/PluginData.kt#L282): 
fixing a delegate type.

```kotlin
inline fun <reified T> PluginData.value(
    default: T,
    crossinline apply: T.() -> Unit = {},
): SerializerAwareValue<@kotlin.internal.Exact T>
// delegate is used like this
var singleMessage: SingleMessage by value(PlainText("str"))
```

But in case of `var` delegated property, the delegate type is invariant anyway.
They want to fix the type for `val` delegated properties?
Maybe so that a proper serializer is used?
They [use](https://github.com/mamoe/mirai/blob/283f8840d4682cc30fbdd87c66fe76f6a71ff8db/mirai-console/backend/mirai-console/src/data/Value.kt#L110)
`Unit` serializer for everything by the way.

---

For synthetic elvis call:

M. Zarechenskiy:

> Speaking of the roots, if I’m not mistaken, the Exact annotation was added for the following reason: 
> it’s possible to specify a type argument for a regular call but not possible to do so with the elvis operator. 
> So, the idea was to say that now it’s possible to control inference via the expected type (I’m not saying that it’s very reasonable…)

## `@NoInfer`

A type (not parameter) annotation. 
Design `@NoInfer` for public use [issue](https://youtrack.jetbrains.com/issue/KT-54642/Expose-NoInfer-annotation-and-design-it-for-public-use).
`@NoInfer` does not work for builders [issue](https://youtrack.jetbrains.com/issue/KT-54477/NoInfer-doesnt-work-for-builders)
(has great use-cases explanation: di and DSLs).

### What It Does

Contraints originated from positions with `@NoInfer` in the constraint system:
- are not considered proper
  - they do not participate in the readiness check for a TV
  - they do not participate in result type resolution for a TV during fixation
- participate normally in incorporation
  - they can generate type inconsistency/mismatch errors
  - new constraints are generated from them
    - `isNoInfer` is propagated to the new constraints

### What It Solves

In the standard library: prohibit inferring a type when its value directly
affects runtime semantics, e.g. values are casted to this type.

```kotlin
inline fun <reified R> Array<*>.filterIsInstance(): List<@kotlin.internal.NoInfer R>
inline fun <reified R> Iterable<*>.filterIsInstance(): List<@kotlin.internal.NoInfer R>
inline fun <reified R> Sequence<*>.filterIsInstance(): Sequence<@kotlin.internal.NoInfer R>

inline fun <reified T> List<*>.castAll(): List<@kotlin.internal.NoInfer T>

context(context: @NoInfer A)
inline fun <A> contextOf(): @NoInfer A = context
```

In tests: prohibit unrelated types?

```kotlin
fun <V> checkDelegate0(delegated: KProperty0<@NoInfer V>, source: KProperty0<V>) {
    assertEquals(delegated.get(), source.get())
}
```

Should not `@OnlyInputTypes` be used instead here?

---

In [intellij-community](https://github.com/JetBrains/intellij-community/blob/4be84000f429bc4deb1ae74d1772fa5ba06b9bcd/plugins/kotlin/util/project-model-updater/src/org/jetbrains/tools/model/updater/PreferenceModification.kt#L22):

```kotlin
internal fun <T> KProperty<T>.modify(
    // Infer the type information only from the receiver to require the argument of the same type
    @Suppress("INVISIBLE_REFERENCE") newValue: @kotlin.internal.NoInfer T
): PreferenceModification<T> = PreferenceModification(this, newValue)
```

`PreferenceModification` is invariant, not sure what is the intention of `@NoInfer`.
Why not use `@Exact` on `KProperty` argument?

---

In [imgui](https://github.com/kotlin-graphics/imgui/blob/665b1032094c4f342a4306c0d3adcc944d87d0e7/core/src/main/kotlin/imgui/flags%20%26%20enumerations.kt#L124):
avoid inferring unrelated types.

```kotlin
fun <Self : Flag<Self>> Flag<Self>.has(b: Flag<@NoInfer Self>): Boolean = and(b).isNotEmpty
```

Seems like a use-case for `@Exact`.

---

In [raptor](https://github.com/fluidsonic/raptor/blob/a191f83a90ca2a8df4a945fbec02e408e96edfc7/modules/bson/sources-jvm/assembly/RaptorBsonComponent.kt#L37)
(not a popular repo): force the user to specify a type explicitly.

```kotlin
public inline fun <reified Value : Any> definition(
    priority: Priority = Priority.normal,
    noinline configure: RaptorBsonDefinitionBuilder<@NoInfer Value>.() -> Unit,
)
```

---

In [ani-api-server](https://github.com/itsharex/ani-api-server/blob/ea39fd0d8f1ab2961f18096535834c7537eca357/server/src/util/query/QueryHelpers.kt#L69):
(dead repo):

```kotlin
inline fun <T> setOnInsert(name: KProperty<T>, value: @kotlin.internal.NoInfer T): List<Bson>
```

Again, seems like a use-case for `@Exact`.

# Conversation with Ivan Canet

Ivan Canet – author of KtMongo. He used `@OnlyInputTypes` in his library,
did not know previously about `@Exact`.

He believes that `@Exact` is better than `@OnlyInputTypes`,
even in cases where usage is not invariant and semantics is equality comparison:

```kotlin
infix fun <V> Field<T, @Exact V>.eq(value: V)
```

It prohibits some usages:

```kotlin
sealed interface Super
class SubA : Super
class SubB : Super

data class Data(val s: Super)
data class ConcreteData(val sa: SubA)

fun findSuper(s: Super) {
    find {
        // Does not compile, Super is not subtype of SubA
        ConcreteData::sa eq s // (1)
    }
}

fun join() {
    find {
        // Does not compile, Super is not subtype of SubA
        ConcreteData::sa eq Data::s // (2)
    }
}
```

For `(1)` Ivan says that it is better to encourage to pattern match on `s` inside Kotlin itself.
For `(2)` Ivan says that such cases do not appear in practice.

He also emphasizes that `@Exact` oftentimes produces much better error messages.

I think an important point here is that DSLs often model systems where equality
has much simpler semantics than in Kotlin, so `@Exact` fits better than 
`@OnlyInputTypes` which approximates more general Kotlin equality.

# Useful links

Typescript `Noinfer<T>` [docs](https://www.typescriptlang.org/docs/handbook/release-notes/typescript-5-4.html?utm_source=chatgpt.com#the-noinfer-utility-type).






