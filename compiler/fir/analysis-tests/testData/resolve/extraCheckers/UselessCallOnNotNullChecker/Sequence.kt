// RUN_PIPELINE_TILL: BACKEND
// WITH_STDLIB

fun test(s: Sequence<Int>) {
    val <!UNUSED_VARIABLE!>foo<!> = s.<!USELESS_CALL_ON_NOT_NULL!>orEmpty()<!>
}

/* GENERATED_FIR_TAGS: functionDeclaration, localProperty, propertyDeclaration */
