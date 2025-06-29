// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER

interface A
class B : A

val String.ext: A
    get() = TODO()

class Cls {
    fun take(arg: B) {}

    fun test(s: String) {
        if (s.ext is B)
            take(s.ext)
    }
}

fun take(arg: Any) {}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, getter, ifExpression, interfaceDeclaration, isExpression,
propertyDeclaration, propertyWithExtensionReceiver, smartcast */
