// RUN_PIPELINE_TILL: BACKEND
fun <T> materialize(): T = throw Exception()

interface A

fun takeA(a: A) {}

fun test() {
    takeA(if(true) materialize() else materialize())
}

/* GENERATED_FIR_TAGS: functionDeclaration, ifExpression, interfaceDeclaration, nullableType, typeParameter */
