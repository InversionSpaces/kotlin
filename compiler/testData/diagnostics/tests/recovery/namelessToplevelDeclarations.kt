// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -REDECLARATION

package<!SYNTAX!><!>

<!CONFLICTING_OVERLOADS, FUNCTION_DECLARATION_WITH_NO_NAME!>fun ()<!> {

}

val<!SYNTAX!><!> : Int = 1


class<!SYNTAX!><!> {

}

interface<!SYNTAX!><!> {

}

object<!SYNTAX!><!> {

}

enum class<!SYNTAX!><!> {}

annotation class<!SYNTAX!><!> {}

/* GENERATED_FIR_TAGS: annotationDeclaration, classDeclaration, enumDeclaration, functionDeclaration, integerLiteral,
interfaceDeclaration, objectDeclaration, propertyDeclaration */
