// RUN_PIPELINE_TILL: FRONTEND

interface Inv
class Impl : Inv

class Scope<InterfaceT, ImplementationT : InterfaceT>(private val implClass: <!UNRESOLVED_REFERENCE!>j<!>.Class<ImplementationT>) {
    fun foo(c: Collection<InterfaceT>) {
        val hm = c.asSequence()
            .filter(implClass::<!UNRESOLVED_REFERENCE!>isInstance<!>)
            .<!CANNOT_INFER_PARAMETER_TYPE!>map<!>(implClass::<!UNRESOLVED_REFERENCE!>cast<!>)
            .<!CANNOT_INFER_PARAMETER_TYPE!>toSet<!>()
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, interfaceDeclaration, localProperty, nullableType,
primaryConstructor, propertyDeclaration, typeConstraint, typeParameter */
