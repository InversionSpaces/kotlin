// RUN_PIPELINE_TILL: BACKEND
// FULL_JDK
// FILE: imm/Map.java
package imm;
public interface Map<K, V> {
    Option<V> get(K k);
}

// FILE: imm/Set.java
package imm;
public interface Set<E> {
}

// FILE: imm/Option.java
package imm;
public interface Option<T> {
    T getOrElse(T other);
}

// FILE: imm/LinkedHashSet.java
package imm;
public class LinkedHashSet<E> implements Set<E> {
    public static <T> LinkedHashSet<T> empty() { return null; }
}

// FILE: main.kt

typealias ImmutableMap<K, V> = imm.Map<K, V>
typealias ImmutableSet<E> = imm.Set<E>
typealias ImmutableLinkedHashSet<E> = imm.LinkedHashSet<E>

private typealias ImmutableMultimap<K, V> = ImmutableMap<K, ImmutableSet<V>>

private fun <K, V> ImmutableMultimap<K, V>.put(key: K, value: V) {
    this[key].getOrElse(ImmutableLinkedHashSet.empty<V>())
}

/* GENERATED_FIR_TAGS: flexibleType, funWithExtensionReceiver, functionDeclaration, javaFunction, javaType, nullableType,
thisExpression, typeAliasDeclaration, typeAliasDeclarationWithTypeParameter, typeParameter */
