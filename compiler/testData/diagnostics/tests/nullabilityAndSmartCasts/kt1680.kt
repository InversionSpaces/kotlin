// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
//KT-1680 Warn if non-null variable is compared to null
package kt1680

fun foo() {
    val x = 1
    if (<!SENSELESS_COMPARISON!>x != null<!>) {} // <-- need a warning here!
    if (<!SENSELESS_COMPARISON!>x == null<!>) {}
    if (<!SENSELESS_COMPARISON!>null != x<!>) {}
    if (<!SENSELESS_COMPARISON!>null == x<!>) {}

    val y : Int? = 1
    if (y != null) {}
    if (y == null) {}
}

/* GENERATED_FIR_TAGS: equalityExpression, functionDeclaration, ifExpression, integerLiteral, localProperty,
nullableType, propertyDeclaration */
