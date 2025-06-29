// RUN_PIPELINE_TILL: BACKEND
// DIAGNOSTICS: -CONTEXT_RECEIVERS_DEPRECATED
// FIR_IDENTICAL
// LANGUAGE: +ContextReceivers

interface Params
interface Logger {
    fun info(message: String)
}
interface LoggingContext {
    val log: Logger // this context provides reference to logger
}

context(LoggingContext)
fun performSomeBusinessOperation(withParams: Params) {
    log.info("Operation has started")
}

/* GENERATED_FIR_TAGS: functionDeclaration, functionDeclarationWithContext, interfaceDeclaration, propertyDeclaration,
stringLiteral */
