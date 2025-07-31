// FIR_IDENTICAL
// RUN_PIPELINE_TILL: FRONTEND
// WITH_EXTRA_CHECKERS

// TODO: Add separate error for refinements?
refinement One = <!CYCLIC_INHERITANCE_HIERARCHY!>Four<!> satisfies { true }
refinement Two = <!CYCLIC_INHERITANCE_HIERARCHY!>One<!> satisfies { true }
refinement Three = <!CYCLIC_INHERITANCE_HIERARCHY!>Two<!> satisfies { true }
refinement Four = <!CYCLIC_INHERITANCE_HIERARCHY!>Three<!> satisfies { true }

/* GENERATED_FIR_TAGS: lambdaLiteral */
