// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// ISSUE: KT-67369

// FILE: lib.kt

interface CoroutineScope

suspend fun <C> coroutineScope(block: CoroutineScope.() -> C): C = TODO()

operator fun <P> Iterable<P>.plus(arg: Iterable<P>): List<P> = TODO()

// FILE: main.kt

val LIST: List<Int> = TODO()

abstract class A : Iterable<A>

internal suspend fun foo(a: A) {
    coroutineScope {
        sequenceOf(a + LIST.map {
            a
        }).forEach {
        }
    }
}

/* GENERATED_FIR_TAGS: additiveExpression, classDeclaration, funWithExtensionReceiver, functionDeclaration,
functionalType, interfaceDeclaration, lambdaLiteral, nullableType, operator, propertyDeclaration, suspend, typeParameter,
typeWithExtension */
