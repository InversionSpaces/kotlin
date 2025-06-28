// FIR_IDENTICAL
// RUN_PIPELINE_TILL: FRONTEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

fun bar(): Int = 42

fun foo(p: Pos) {}

fun main() {
    val v = bar()
    (v as? Pos)?.let { foo(it) }
}

/* GENERATED_FIR_TAGS: comparisonExpression, functionDeclaration, integerLiteral, lambdaLiteral, localProperty,
nullableType, propertyDeclaration, safeCall */
