// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_EXPRESSION
inline public fun reg(converter: (Any) -> Any, str: String) {
    str
    converter("")
}

public inline fun register(converter: (Any) -> Any) {
    "123$<!USAGE_IS_NOT_INLINABLE!>converter<!>"
    reg(converter, "123$<!USAGE_IS_NOT_INLINABLE!>converter<!>")
}

/* GENERATED_FIR_TAGS: functionDeclaration, functionalType, inline, stringLiteral */
