// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
val <<!REIFIED_TYPE_PARAMETER_NO_INLINE!>reified<!> T> T.v: T
    get() = throw UnsupportedOperationException()

/* GENERATED_FIR_TAGS: getter, nullableType, propertyDeclaration, propertyWithExtensionReceiver, reified, typeParameter */
