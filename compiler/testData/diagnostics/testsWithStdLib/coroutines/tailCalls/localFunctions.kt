// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
suspend fun baz() = 1
suspend fun unit() {}

suspend fun foo() {
    suspend fun bar() {
        baz()
        return unit()
    }

    suspend fun foobar1(): Int {
        return baz()
    }

    suspend fun foobar2() {
        return unit()
    }
}

/* GENERATED_FIR_TAGS: functionDeclaration, integerLiteral, localFunction, suspend */
