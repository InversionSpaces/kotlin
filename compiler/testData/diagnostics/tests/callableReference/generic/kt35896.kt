// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER
// ISSUE: KT-35896

interface B<E, SC>

class Inv<T>

class Foo<T>(x: Int): B<T, Inv<T>>

fun <T1, T2, S> bar(list: T2, fn: (S) -> B<T1, T2>) {}

fun <K> foo(list: Inv<K>) {
    bar(list, ::Foo)
}

/* GENERATED_FIR_TAGS: callableReference, classDeclaration, functionDeclaration, functionalType, interfaceDeclaration,
nullableType, primaryConstructor, typeParameter */
