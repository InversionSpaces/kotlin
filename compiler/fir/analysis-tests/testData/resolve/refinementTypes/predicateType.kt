// FIR_IDENTICAL
// RUN_PIPELINE_TILL: FRONTEND
// WITH_EXTRA_CHECKERS

refinement Ok = Int satisfies { it > 0 }
refinement ReturnTypeMismatch1 = Int satisfies { <!RETURN_TYPE_MISMATCH!>"blahblah"<!> }
refinement ReturnTypeMismatch2 = Int satisfies { <!UNUSED_ANONYMOUS_PARAMETER!>x<!> -> <!RETURN_TYPE_MISMATCH!>42<!> }
refinement ReturnTypeMismatch3 = Int satisfies { <!UNUSED_ANONYMOUS_PARAMETER!>x<!>: Int -> <!RETURN_TYPE_MISMATCH!>42<!> }
refinement PredicateTypeMismatch1 = Int satisfies <!REFINEMENT_PREDICATE_TYPE_MISMATCH!>{ x: Boolean -> x }<!>
refinement PredicateTypeMismatch2 = Int satisfies <!REFINEMENT_PREDICATE_TYPE_MISMATCH!>{ x: String -> x }<!>

/* GENERATED_FIR_TAGS: comparisonExpression, integerLiteral, lambdaLiteral, stringLiteral */
