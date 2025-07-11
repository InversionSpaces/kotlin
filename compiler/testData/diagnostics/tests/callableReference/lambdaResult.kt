// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// SKIP_TXT
// DIAGNOSTICS: -UNUSED_PARAMETER

interface Inv<T>

fun <E> Inv<E>.foo(
    handler: () -> ((command: E) -> Unit)
) {}

fun bar(x: Int) {}
fun bar(x: String) {}

fun bar1(arg: Int) {}
fun foo1(f: () -> (Int) -> Unit) = ""

fun main(x: Inv<Int>) {
    x.foo<Int> {
        if (x.hashCode() == 0) return@foo ::bar

        ::bar
    }

    x.foo {
        if (x.hashCode() == 0) return@foo ::bar

        ::bar
    }

    foo1 {
        ::bar1
    }

    foo1 {
        return@foo1 ::bar1
    }

    foo1 {
        if (x.hashCode() == 0) return@foo1 ::bar

        ::bar
    }
}

/* GENERATED_FIR_TAGS: callableReference, equalityExpression, funWithExtensionReceiver, functionDeclaration,
functionalType, ifExpression, integerLiteral, interfaceDeclaration, lambdaLiteral, nullableType, stringLiteral,
typeParameter */
