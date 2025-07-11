// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_VARIABLE
// LANGUAGE: +SamConversionForKotlinFunctions +SamConversionPerArgument +ProhibitVarargAsArrayAfterSamArgument
// SKIP_TXT

// FILE: Test.java
public class Test {
    public static String foo1(Runnable r, String... strs) {
        return null;
    }
    public String foo2(Runnable r, Runnable r, String... strs) {
        return null;
    }
    public Test(Runnable r, String... strs) {}
    public Test(Runnable r1, Runnable r2, String... strs) {}
}

// FILE: main.kt
fun main(x2: Runnable) {
    val x1 = {}
    val x3 = arrayOf<String>()

    Test.foo1({}, <!TYPE_MISMATCH!>arrayOf()<!>)
    Test.foo1({}, *arrayOf())
    Test.foo1({}, *x3)
    Test.foo1({}, <!TYPE_MISMATCH!>arrayOf("")<!>)

    Test.foo1(x1, <!TYPE_MISMATCH!>arrayOf()<!>)
    Test.foo1(x1, *arrayOf())
    Test.foo1(x2, <!TYPE_MISMATCH!>arrayOf()<!>)
    Test.foo1(x2, *arrayOf())

    Test.foo1(x1, <!TYPE_MISMATCH!>x3<!>)
    Test.foo1(x1, *x3)
    Test.foo1(x2, <!TYPE_MISMATCH!>arrayOf("")<!>)
    Test.foo1(x2, *arrayOf(""))

    val i1 = <!NONE_APPLICABLE!>Test<!>({}, arrayOf())
    val i2 = Test({}, *arrayOf())
    val i3 = <!NONE_APPLICABLE!>Test<!>({}, x3)
    val i4 = <!NONE_APPLICABLE!>Test<!>({}, arrayOf(""))
    val i5 = Test({}, {}, *arrayOf(""))
    val i6 = <!NONE_APPLICABLE!>Test<!>({}, {}, arrayOf())

    i2.foo2({}, {}, <!TYPE_MISMATCH!>arrayOf()<!>)
    i2.foo2({}, {}, *arrayOf())
    i2.foo2({}, x2, <!TYPE_MISMATCH!>arrayOf()<!>)
    i2.foo2(x2, {}, *arrayOf())

    i2.foo2({}, {}, <!TYPE_MISMATCH!>arrayOf("")<!>)
    i2.foo2({}, {}, *x3)
    i2.foo2({}, x2, <!TYPE_MISMATCH!>x3<!>)
    i2.foo2(x2, {}, *arrayOf(""))
}

/* GENERATED_FIR_TAGS: flexibleType, functionDeclaration, javaFunction, javaType, lambdaLiteral, localProperty,
propertyDeclaration, samConversion, stringLiteral */
