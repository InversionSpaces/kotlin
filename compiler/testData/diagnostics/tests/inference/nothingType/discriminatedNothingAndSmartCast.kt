// RUN_PIPELINE_TILL: BACKEND
// DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_VARIABLE

interface ExpectedType

inline fun <reified M> parse(): M? = TODO()

fun test(s: String?, silent: Boolean) {
    val result: ExpectedType =
        if (s != null) {
            <!DEBUG_INFO_EXPRESSION_TYPE("ExpectedType?")!>parse()<!> ?: TODO()
        } else <!DEBUG_INFO_EXPRESSION_TYPE("kotlin.Nothing")!>if (silent) {
            return
        } else {
            throw Exception()
        }<!>

    <!DEBUG_INFO_SMARTCAST!>s<!>.length
}

/* GENERATED_FIR_TAGS: elvisExpression, equalityExpression, functionDeclaration, ifExpression, inline,
interfaceDeclaration, localProperty, nullableType, propertyDeclaration, reified, smartcast, typeParameter */
