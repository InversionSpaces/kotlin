// RUN_PIPELINE_TILL: BACKEND
// LANGUAGE: +EnumEntries -PrioritizedEnumEntries
// WITH_STDLIB
import MyEnum.entries

enum class MyEnum

val entries = "local str"

fun test() {
    val s: String = <!DEPRECATED_ACCESS_TO_ENTRIES_PROPERTY!>entries<!>
}

/* GENERATED_FIR_TAGS: enumDeclaration, functionDeclaration, localProperty, propertyDeclaration, stringLiteral */
