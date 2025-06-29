// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// SKIP_TXT

// FILE: lib.kt
package lib
fun foo(x: Double): Double = x

// FILE: main.kt

import lib.*

fun foo(): Map<Int, String> = TODO()

var z: (x: Double) -> Double = { it }

fun bar() {
    z = ::foo
}

fun baz(x: (Double) -> Double) {}

/* GENERATED_FIR_TAGS: assignment, callableReference, functionDeclaration, functionalType, lambdaLiteral,
propertyDeclaration */
