// RUN_PIPELINE_TILL: FIR2IR
// LANGUAGE: +ContextReceivers

class A
class B

context(A)
fun topLevelFun() {}

context(A, B)
fun topLevelFunTwoReceivers() {}

context(A)
var varProp: Int
    get() = 42
    set(newVal) {}

context(A)
val valProp: Int get() = 42

context(A)
class Clazz {
    context(A)
    fun memberFun() {}

    context(A)
    fun B.memberExtFun() {}

    context(A)
    var varProp: Int
        get() = 42
        set(newVal) {}

    context(A)
    val valProp: Int get() = 42
}

context(A)
class Clazz2 {
    constructor()
}

class Clazz3 {
    context(<!DEBUG_INFO_MISSING_UNRESOLVED!>A<!>)
    constructor()
}

fun typeRef(body: context(A) () -> Unit): context(A) () -> Unit {
    val x: context(A) () -> Unit = body
    val y = body
    val z: suspend context(A) B.() -> Unit = {}
    val w: (context(Int) () -> Unit, context(Int) () -> Unit) -> (context(Int) () -> Unit) = { a, b -> { } }
    return {}
}

typealias typealiasDecl = context(A) B.() -> Unit

context(A)
fun Clazz.ext() {}

context(A)
val Clazz.extVal: Int get() = 904

context(A)
var Clazz.extVar: Int
    get() = 904
    set(newVal) {}

context(A)
interface I {}

context(A, B)
class ClazzTwoReceivers {}

context(A)
enum class E

context(A)
object O

/* GENERATED_FIR_TAGS: classDeclaration, enumDeclaration, funWithExtensionReceiver, functionDeclaration,
functionDeclarationWithContext, functionalType, getter, integerLiteral, interfaceDeclaration, lambdaLiteral,
localProperty, objectDeclaration, propertyDeclaration, propertyDeclarationWithContext, propertyWithExtensionReceiver,
secondaryConstructor, setter, suspend, typeAliasDeclaration, typeWithContext, typeWithExtension */
