// RUN_PIPELINE_TILL: FRONTEND
class A(foo: Int.() -> Unit) {
    init {
        4.foo()
    }
}

fun test(foo: Int.(String) -> Unit) {
    4.foo("")
    4.foo(<!NO_VALUE_FOR_PARAMETER!><!NAMED_ARGUMENTS_NOT_ALLOWED, NAMED_PARAMETER_NOT_FOUND!>p1<!> = "")<!>
    4.foo(<!NAMED_ARGUMENTS_NOT_ALLOWED!>p2<!> = "")
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, functionalType, init, integerLiteral, primaryConstructor,
stringLiteral, typeWithExtension */
