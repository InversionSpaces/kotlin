// RUN_PIPELINE_TILL: FRONTEND
// LANGUAGE: +ErrorAboutDataClassCopyVisibilityChange, -DataClassCopyRespectsConstructorVisibility
data class Data <!DATA_CLASS_COPY_VISIBILITY_WILL_BE_CHANGED_ERROR!>private<!> constructor(val x: Int) {
    fun copy() = Data(1)
}

fun local(data: Data) {
    data.copy()
}

/* GENERATED_FIR_TAGS: classDeclaration, data, functionDeclaration, integerLiteral, primaryConstructor,
propertyDeclaration */
