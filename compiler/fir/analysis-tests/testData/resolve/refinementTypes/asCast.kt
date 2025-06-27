// FIR_IDENTICAL
// RUN_PIPELINE_TILL: FRONTEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

fun bar(): Int = 42

fun foo(p: Pos) {}

fun main() {
    val v = bar()
    if (v > 0) {
        foo(v as Pos)
    }
}

/* GENERATED_FIR_TAGS: asExpression, comparisonExpression, functionDeclaration, ifExpression, integerLiteral,
lambdaLiteral, localProperty, propertyDeclaration */
