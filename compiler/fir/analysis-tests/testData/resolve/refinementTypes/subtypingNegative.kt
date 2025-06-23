// FIR_IDENTICAL
// RUN_PIPELINE_TILL: FRONTEND
// WITH_EXTRA_CHECKERS

typealias Pos = Int satisfies { it > 0 }

fun bar(): Int = 42

fun foo(i: Pos) {}

fun main() {
    val v = bar()
    foo(<!ARGUMENT_TYPE_MISMATCH!>v<!>)
}
