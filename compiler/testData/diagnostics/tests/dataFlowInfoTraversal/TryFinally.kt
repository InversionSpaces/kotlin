// RUN_PIPELINE_TILL: FRONTEND
// CHECK_TYPE

fun tryFinally(x: Int?) {
    try {
    } finally {
        x!!
    }
    checkSubtype<Int>(<!DEBUG_INFO_SMARTCAST!>x<!>)
}

fun tryCatchFinally(x: Int?) {
    try {
        x!!
    } catch (e: Exception) {
        x!!
    } finally {
        checkSubtype<Int>(<!TYPE_MISMATCH!>x<!>)
        x!!
    }
    checkSubtype<Int>(<!DEBUG_INFO_SMARTCAST!>x<!>)
}

/* GENERATED_FIR_TAGS: checkNotNullCall, classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType,
infix, localProperty, nullableType, propertyDeclaration, smartcast, tryExpression, typeParameter, typeWithExtension */
