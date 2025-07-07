// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// WITH_EXTRA_CHECKERS

refinement Even = Int satisfies { it % 2 == 0 }

fun Int.isSmall(): Boolean = this < 16 && this > -16

refinement SmallEven = Even satisfies { it.isSmall() }
refinement NonNullSmall = SmallEven satisfies { it != 0 }

/* GENERATED_FIR_TAGS: funWithExtensionReceiver, functionDeclaration, lambdaLiteral */
