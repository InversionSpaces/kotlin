// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// WITH_EXTRA_CHECKERS
// CHECK_TYPE_WITH_EXACT

refinement Pos = Int satisfies { it > 0 }

fun Pos.test(): String = "test"
fun Int.test(): Boolean = false

fun test(a: Pos, b: Pos) {
    checkExactType<String>(a.test())
}

/* GENERATED_FIR_TAGS: additiveExpression, asExpression, classDeclaration, comparisonExpression,
funWithExtensionReceiver, functionDeclaration, functionalType, infix, integerLiteral, lambdaLiteral, nullableType,
operator, typeParameter, typeWithExtension */
