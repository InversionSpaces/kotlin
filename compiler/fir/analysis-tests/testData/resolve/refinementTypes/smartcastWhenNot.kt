// FIR_IDENTICAL
// RUN_PIPELINE_TILL: FRONTEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

fun bar(): Int = 42

fun foo(p: Pos) {}

fun main() {
    val v = bar()
    when (v) {
        !is Pos -> {}
        else -> foo(v)
    }
}

/* GENERATED_FIR_TAGS: comparisonExpression, functionDeclaration, integerLiteral, isExpression, lambdaLiteral,
localProperty, propertyDeclaration, smartcast, whenExpression, whenWithSubject */
