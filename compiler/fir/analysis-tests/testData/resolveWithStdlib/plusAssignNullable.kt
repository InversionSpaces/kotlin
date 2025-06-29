// RUN_PIPELINE_TILL: BACKEND
interface A {
    val list: List<String>
}

interface B {
    val list: MutableList<String>
}

fun B.foo(a: A?) {
    list.plusAssign(mutableListOf(""))
    with(a) {
        list.plusAssign(mutableListOf(""))
        list += mutableListOf("")
    }
}

/* GENERATED_FIR_TAGS: funWithExtensionReceiver, functionDeclaration, interfaceDeclaration, lambdaLiteral, nullableType,
propertyDeclaration, stringLiteral */
