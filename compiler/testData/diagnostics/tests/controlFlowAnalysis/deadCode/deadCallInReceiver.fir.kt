// RUN_PIPELINE_TILL: BACKEND
fun test11() {
    fun Any.bar(i: Int) {}
    todo()<!UNREACHABLE_CODE!>.bar(1)<!>
}

fun test12() {
    fun Any.bar(i: Int) {}
    todo()<!UNREACHABLE_CODE!><!UNNECESSARY_SAFE_CALL!>?.<!>bar(1)<!>
}

fun todo(): Nothing = throw Exception()

/* GENERATED_FIR_TAGS: funWithExtensionReceiver, functionDeclaration, integerLiteral, localFunction, nullableType,
safeCall */
