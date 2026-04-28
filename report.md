# Kotlin Type Inference Manipulation Mechanisms

Comprehensive reference of annotations and compiler mechanisms that control/restrict/modify generic type parameter inference.

---

## 1. Annotation Definitions

All internal inference annotations live in:
**`libraries/stdlib/src/kotlin/internal/Annotations.kt`**

FqName constants: **`core/compiler.common/src/org/jetbrains/kotlin/resolve/descriptorUtil/annotationsForResolve.kt`**

---

### `@OnlyInputTypes` — Type parameter annotation

```kotlin
// Annotations.kt:52
@Target(AnnotationTarget.TYPE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
internal annotation class OnlyInputTypes
```

**Effect:** The inferred value of this type parameter must match a type that appeared as an *input* to the call — i.e., as an argument type, receiver type, or expected type. If the compiler would infer a type not mentioned in inputs (e.g. an intersection or synthesized type), it reports `TYPE_INFERENCE_ONLY_INPUT_TYPES_ERROR`.

**Scope:** `kotlin.internal` — internal stdlib annotation, not part of public API.

**Key stdlib usages:** Collection functions like `contains`, `containsKey`, `indexOf` — prevents inference of unrelated types from unification.

---

### `@Exact` — Use-site type annotation

```kotlin
// Annotations.kt:30
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
internal annotation class Exact
```

**Effect:** Forces an *equality* constraint (instead of subtype) for the annotated type during inference. Normally subtyping allows variance; `@Exact` pins the type variable to exactly this type.

**Mechanism:** In `TypeCheckerStateForConstraintSystem.addSubtypeConstraint` (line 79–95), when either side carries `@Exact`, the constraint system adds **both** `sub <: super` and `super <: sub`, effectively creating `sub == super`.

---

### `@NoInfer` — Use-site type annotation

```kotlin
// Annotations.kt:23
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
internal annotation class NoInfer
```

**Effect:** The annotated type site is excluded from contributing constraints to type inference. The type is still checked for compatibility after inference, but it does not drive the inference itself.

**K1 behaviour:** If either subtype or supertype carries `@NoInfer`, `addSubtypeConstraint` returns early (line 75–76 in `TypeCheckerStateForConstraintSystem`).

**K2 behaviour:** The `isNoInfer` flag is passed through the constraint chain and tracked per-constraint (`Constraint.isNoInfer`); the constraint is added but tagged so it doesn't participate in incorporation.

---

### `@BuilderInference` — Parameter/function annotation (deprecated)

```kotlin
// Inference.kt:33
@Target(VALUE_PARAMETER, FUNCTION, PROPERTY)
@Retention(AnnotationRetention.BINARY)
@Deprecated("...enabled automatically for builder calls if needed.")
@DeprecatedSinceKotlin(warningSince = "2.0", errorSince = "2.5", hiddenSince = "2.6")
public annotation class BuilderInference
```

**Effect:** Historically enabled "builder inference" — inferring type arguments from calls made inside a lambda parameter. Now automatic; this annotation is defunct.

---

### `@OverloadResolutionByLambdaReturnType` — Function annotation

```kotlin
// Inference.kt:67
@Target(FUNCTION)
@Retention(AnnotationRetention.BINARY)
@ExperimentalTypeInference
public annotation class OverloadResolutionByLambdaReturnType
```

**Effect:** When multiple overloads differ only in the return type of a lambda parameter, this annotation enables disambiguation by analyzing the lambda return type. Not inference restriction per se, but affects how the resolver chooses among overloads.

---

### `@LowPriorityInOverloadResolution` / `@HidesMembers` — Resolution priority

```kotlin
// Annotations.kt:37
internal annotation class LowPriorityInOverloadResolution  // deprioritizes in overload resolution

// Annotations.kt:45
internal annotation class HidesMembers  // extension wins over member (only forEach, addSuppressed)
```

These affect *overload resolution order* rather than type inference directly, but interact with inference when selecting which candidate to resolve.

---

## 2. How `@OnlyInputTypes` Works (End-to-End)

### 2.1 Type Variable Creation

`compiler/resolution/src/org/jetbrains/kotlin/resolve/calls/inference/model/TypeVariable.kt:80`

```kotlin
class TypeVariableFromCallableDescriptor(...) : NewTypeVariable(...) {
    override fun hasOnlyInputTypesAnnotation() =
        originalTypeParameter.hasOnlyInputTypesAnnotation()
}
```

Only `TypeVariableFromCallableDescriptor` (created from actual type parameters) carries the flag. Synthetic type variables for lambdas/callables do not.

### 2.2 Constraint Positions that Count as "Input"

`compiler/resolution.common/src/org/jetbrains/kotlin/resolve/calls/inference/model/ConstraintPositionAndErrors.kt:16`

The marker interface `OnlyInputTypeConstraintPosition` is implemented by:

| Position | Meaning |
|---|---|
| `ExplicitTypeParameterConstraintPosition` | Explicitly written type argument |
| `ExpectedTypeConstraintPosition` | Expected type at call site |
| `ReceiverConstraintPosition` | Receiver argument |
| `RegularArgumentConstraintPosition` | Regular value argument |
| `CallableReferenceConstraintPosition` | Callable reference argument |
| `BuilderInferenceSubstitutionConstraintPosition` | Builder inference injection |
| `InjectedAnotherStubTypeConstraintPosition` | Stub type injection |

### 2.3 Collecting Input Types

`compiler/resolution.common/src/org/jetbrains/kotlin/resolve/calls/inference/model/MutableConstraintStorage.kt:55`

```kotlin
fun getProjectedInputCallTypes(...): Collection<Pair<KotlinTypeMarker, ConstraintKind>> {
    return mutableConstraints.mapNotNullTo(...) {
        if (it.position.from is OnlyInputTypeConstraintPosition || it.inputTypePositionBeforeIncorporation != null)
            it.type.unCapture() to it.kind
        else null
    }
}
```

Only constraints from input positions are candidates for the allowed set.

### 2.4 Postponed Validation

`compiler/resolution.common/src/org/jetbrains/kotlin/resolve/calls/inference/model/NewConstraintSystemImpl.kt:748`

When a `@OnlyInputTypes` variable is fixed, validation is **deferred** until all type variables are fixed (to allow substitution of remaining variables in input types):

```kotlin
fun postponeOnlyInputTypesCheck(variableWithConstraints, resultType) {
    postponedComputationsAfterAllVariablesAreFixed.add {
        checkOnlyInputTypesAnnotation(variableWithConstraints, resultType)
    }
}
```

### 2.5 Validation Logic

`NewConstraintSystemImpl.kt:782`

```kotlin
fun checkOnlyInputTypesAnnotation(variableWithConstraints, resultType) {
    val projectedInputCallTypes = variableWithConstraints.getProjectedInputCallTypes(utilContext)
    val isResultTypeEqualSomeInputType = projectedInputCallTypes.any { (inputType, constraintKind) ->
        // substitute remaining vars, approximate, then compare with resultType
        val otherResultType = inputType.substituteAndApproximateIfNecessary(...)
        resultType.typeConstructor() == otherResultType.typeConstructor()
        // also checks supertypes for intersection types
    }
    if (!isResultTypeEqualSomeInputType) addError(OnlyInputTypesDiagnostic(variable))
}
```

### 2.6 Error Reporting

- **Diagnostic class:** `OnlyInputTypesDiagnostic` — `ConstraintPositionAndErrors.kt:180`
- **FIR error:** `TYPE_INFERENCE_ONLY_INPUT_TYPES_ERROR` — `FirErrors.kt:479`
- **Severity:** `INAPPLICABLE` (call not resolved)

---

## 3. K1 vs K2 Differences

### `@NoInfer`

- **K1:** Constraint is skipped entirely (early return in `addSubtypeConstraint`).
- **K2:** Constraint is added but flagged with `isNoInfer: Boolean`; it participates in the system but is excluded from constraint incorporation.

### `@OnlyInputTypes` — captured type unwrapping

`compiler/fir/resolve/src/org/jetbrains/kotlin/fir/resolve/inference/ConeConstraintSystemUtilContext.kt:70`

K1 unwraps captured types (e.g. `Captured(in T)` → `T`) before matching against input types. K2 intentionally skips this unwrapping (`unCapture()` is a no-op), leading to some K1-red / K2-green divergences. Rationale documented in a long comment at that location.

**K2 check** (`ConeConstraintSystemUtilContext.kt:37`):
```kotlin
override fun TypeVariableMarker.hasOnlyInputTypesAttribute(): Boolean {
    if (this !is ConeTypeParameterBasedTypeVariable) return false
    return typeParameterSymbol.resolvedAnnotationClassIds.any { it == StandardClassIds.Annotations.OnlyInputTypes }
}
```

---

## 4. Key File Reference

| Topic | File |
|---|---|
| Annotation definitions | `libraries/stdlib/src/kotlin/internal/Annotations.kt` |
| Public inference annotations | `libraries/stdlib/src/kotlin/annotations/Inference.kt` |
| FqName constants + utility fns | `core/compiler.common/src/org/jetbrains/kotlin/resolve/descriptorUtil/annotationsForResolve.kt` |
| StandardClassIds for annotations | `core/names/src/org/jetbrains/kotlin/name/StandardClassIds.kt:223,229` |
| Constraint positions & errors | `compiler/resolution.common/.../inference/model/ConstraintPositionAndErrors.kt` |
| `@Exact`/`@NoInfer` constraint processing | `compiler/resolution.common/.../inference/components/TypeCheckerStateForConstraintSystem.kt` |
| `@OnlyInputTypes` validation | `compiler/resolution.common/.../inference/model/NewConstraintSystemImpl.kt:748–800` |
| Input type collection | `compiler/resolution.common/.../inference/model/MutableConstraintStorage.kt:55` |
| K1 type variable model | `compiler/resolution/.../inference/model/TypeVariable.kt` |
| K2 util context (FIR) | `compiler/fir/resolve/.../inference/ConeConstraintSystemUtilContext.kt` |
| FIR diagnostic definitions | `compiler/fir/checkers/gen/.../diagnostics/FirErrors.kt:479` |
| Test data | `compiler/testData/diagnostics/testsWithStdLib/inference/annotationsForResolve/` |
| Architecture overview | `docs/fir/inference.md` |

---

## 5. Summary Table

| Annotation | Target | Effect on Inference |
|---|---|---|
| `@OnlyInputTypes` | Type parameter | Inferred type must appear in call inputs; else error |
| `@Exact` | Type use-site | Forces equality constraint instead of subtype constraint |
| `@NoInfer` | Type use-site | Excludes this type from contributing to inference |
| `@BuilderInference` | Parameter | (Deprecated) Builder inference from lambda body |
| `@OverloadResolutionByLambdaReturnType` | Function | Overload disambiguation by lambda return type |
| `@LowPriorityInOverloadResolution` | Function/property | Deprioritized in overload resolution |
| `@HidesMembers` | Function/property | Extension beats member with same name |
