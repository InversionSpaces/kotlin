// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_PARAMETER
// CHECK_TYPE
// See KT-9893
open class A

public interface I<T : A> {
    public fun foo(): T?
}

fun acceptA(a: A) {
}

fun main(i: I<*>) {
    i.foo() checkType { _<A?>() }
    acceptA(<!TYPE_MISMATCH!>i.foo()<!>) // i.foo() should be nullable but isn't
}

/* GENERATED_FIR_TAGS: capturedType, classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType,
infix, interfaceDeclaration, lambdaLiteral, nullableType, starProjection, typeConstraint, typeParameter,
typeWithExtension */
