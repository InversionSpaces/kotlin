// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

fun bar(): Int = 42

fun foo(p: Pos) {}

fun main() {
    val v = bar()
    if (v is Pos) {
        foo(v)
    }
}

/* GENERATED_FIR_TAGS: comparisonExpression, functionDeclaration, ifExpression, integerLiteral, isExpression,
lambdaLiteral, localProperty, propertyDeclaration, smartcast */
