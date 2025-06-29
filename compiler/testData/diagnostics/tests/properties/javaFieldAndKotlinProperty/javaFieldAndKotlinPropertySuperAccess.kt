// RUN_PIPELINE_TILL: FRONTEND
// LANGUAGE: -ProperFieldAccessGenerationForFieldAccessShadowedByKotlinProperty
// ISSUE: KT-56386

// FILE: Jaba.java
public class Jaba {
    public String a = "O";
    public String b = "";
}

// FILE: test.kt
open class My : Jaba() {
    private val a: String = "FAIL"
    private val b: String = "FAIL"
}

class Some : My() {
    fun soo(): String {
        super<My>.b = "K"
        return super<My>.a + super<My>.b
    }
}

fun box(): String = Some().soo()

/* GENERATED_FIR_TAGS: additiveExpression, assignment, classDeclaration, flexibleType, functionDeclaration, javaProperty,
javaType, propertyDeclaration, stringLiteral, superExpression */
