// RUN_PIPELINE_TILL: FRONTEND
class C {
    private companion object
}

typealias CAlias = C

val <!EXPOSED_PROPERTY_TYPE!>test1<!> = <!INVISIBLE_MEMBER!>CAlias<!>
val <!EXPOSED_PROPERTY_TYPE!>test1a<!> = <!INVISIBLE_MEMBER!>C<!>

/* GENERATED_FIR_TAGS: classDeclaration, companionObject, objectDeclaration, propertyDeclaration, typeAliasDeclaration */
