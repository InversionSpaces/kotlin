// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: BACKEND
// LANGUAGE: +MultiPlatformProjects
// ISSUE: KT-58845

// MODULE: common
// FILE: common.kt
expect interface I1

expect interface I2

expect interface I3

expect interface I4

<!FUN_INTERFACE_WRONG_COUNT_OF_ABSTRACT_MEMBERS{JVM}!>fun<!> interface F1 : I1 {
    fun foo()
}

<!FUN_INTERFACE_ABSTRACT_METHOD_WITH_TYPE_PARAMETERS{JVM}, FUN_INTERFACE_WRONG_COUNT_OF_ABSTRACT_MEMBERS!>fun<!> interface F2 : I2 {}

<!FUN_INTERFACE_WRONG_COUNT_OF_ABSTRACT_MEMBERS{JVM}!>fun<!> interface F3 : I3 {
    fun foo()
}

<!FUN_INTERFACE_ABSTRACT_METHOD_WITH_TYPE_PARAMETERS{JVM}, FUN_INTERFACE_WRONG_COUNT_OF_ABSTRACT_MEMBERS!>fun<!> interface F4 : I4 {}

// MODULE: jvm()()(common)
// FILE: J1.java
public interface J1 {
    public void bar();
}

// FILE: J2.java
public interface J2 {
    public <T> T bar();
}

// FILE: main.kt
actual interface I1 : J1 {}

actual interface I2 : J2 {}

actual typealias I3 = J1

actual typealias I4 = J2

/* GENERATED_FIR_TAGS: actual, expect, funInterface, functionDeclaration, interfaceDeclaration, javaType,
typeAliasDeclaration */
