// FIR_IDENTICAL
// RUN_PIPELINE_TILL: FRONTEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

fun bar(): Pos = 42 as Pos

fun foo(i: Int) {}

fun main() {
    val v = bar()
    foo(v)
}

/* GENERATED_FIR_TAGS: asExpression, comparisonExpression, functionDeclaration, integerLiteral, lambdaLiteral,
localProperty, propertyDeclaration */
