// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: FIR2IR
// MODULE: m1-common
// FILE: common.kt

interface A

class B : A
expect class <!NO_ACTUAL_FOR_EXPECT{JVM}!>Foo<!>(b: B) : <!IMPLEMENTATION_BY_DELEGATION_IN_EXPECT_CLASS, IMPLEMENTATION_BY_DELEGATION_IN_EXPECT_CLASS{JVM}!>A by b<!>

expect class <!NO_ACTUAL_FOR_EXPECT{JVM}!>Bar<!> : <!IMPLEMENTATION_BY_DELEGATION_IN_EXPECT_CLASS, IMPLEMENTATION_BY_DELEGATION_IN_EXPECT_CLASS{JVM}!>A by B()<!>

// MODULE: m1-jvm()()(m1-common)

/* GENERATED_FIR_TAGS: classDeclaration, expect, inheritanceDelegation, interfaceDeclaration, primaryConstructor */
