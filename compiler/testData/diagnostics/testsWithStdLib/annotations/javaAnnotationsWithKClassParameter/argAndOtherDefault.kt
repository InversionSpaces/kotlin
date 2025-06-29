// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// FILE: A.java
public @interface A {
    Class<?> arg();
    int x() default 1;
}

// FILE: b.kt
@A(arg = String::class) class MyClass1
@A(arg = String::class, x = 2) class MyClass2

/* GENERATED_FIR_TAGS: classDeclaration, classReference, integerLiteral, javaType */
