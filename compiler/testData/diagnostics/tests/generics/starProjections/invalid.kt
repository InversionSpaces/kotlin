// RUN_PIPELINE_TILL: FRONTEND
// CHECK_TYPE
// DIAGNOSTICS: -UNUSED_PARAMETER
// FILE: main.kt
class Inv<T>(val x: T)

class A<T : Inv<in T>> {
    fun foo(): T = null!!
}

class Inv2<<!FINITE_BOUNDS_VIOLATION!>T : Inv2<in T><!>>(val x: T)

fun main(a: A<*>, j: JavaClass<*>, i2: Inv2<*>) {
    // Probably it's too restrictive to suppose star projection type here as Any?,
    // but looks like we can refine it later
    a.foo() checkType { _<Any?>() }
    j.foo() checkType { _<Any?>() }
    i2.x checkType { _<Any?>() }

    j.bar(<!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>, <!CONSTANT_EXPECTED_TYPE_MISMATCH!>2<!>, <!TYPE_MISMATCH!>Any()<!>)
    j.bar(null)
}

// FILE: JavaClass.java
public class JavaClass<T extends JavaClass<? super T>> {
    public void bar(T... x) {}
    public T foo() {}
}

/* GENERATED_FIR_TAGS: capturedType, checkNotNullCall, classDeclaration, flexibleType, funWithExtensionReceiver,
functionDeclaration, functionalType, inProjection, infix, integerLiteral, javaType, lambdaLiteral, nullableType,
outProjection, primaryConstructor, propertyDeclaration, starProjection, typeConstraint, typeParameter, typeWithExtension */
