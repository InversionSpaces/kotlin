// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// WITH_EXTRA_CHECKERS
// WITH_STDLIB

refinement Pos = Int satisfies { it > 0 }

fun sum(vararg positives: Pos): Int = positives.sum()

fun main() {
    val a = 42 as Pos
    sum(a, 37 as Pos)
}

/* GENERATED_FIR_TAGS: asExpression, comparisonExpression, functionDeclaration, integerLiteral, lambdaLiteral,
localProperty, outProjection, propertyDeclaration, vararg */
