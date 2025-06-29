// RUN_PIPELINE_TILL: FRONTEND
// INFERENCE_HELPERS
// ISSUE: KT-57036

abstract class Base(block: String.() -> Int)
class A(block: String.() -> Int) : Base(block)
class B(block: String.() -> Int) : Base(block)

fun test_1() {
    val c = select(::A, ::B)
    c { length }
    c { <!UNRESOLVED_REFERENCE!>it<!>.length }
}

fun test_2(cond: Boolean) {
    val c = if (cond) ::A else ::B
    c { length }
    c { <!UNRESOLVED_REFERENCE!>it<!>.length }
}

fun test_3(cond: Boolean) {
    val c = when(cond) {
        true -> ::A
        false -> ::B
    }
    c { length }
    c { <!UNRESOLVED_REFERENCE!>it<!>.length }
}

/* GENERATED_FIR_TAGS: callableReference, capturedType, checkNotNullCall, classDeclaration, equalityExpression,
functionDeclaration, functionalType, ifExpression, integerLiteral, lambdaLiteral, localProperty, nullableType,
outProjection, primaryConstructor, propertyDeclaration, smartcast, typeParameter, typeWithExtension, vararg,
whenExpression, whenWithSubject */
