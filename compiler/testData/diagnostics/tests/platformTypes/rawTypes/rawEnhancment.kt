// DISABLE_JAVA_FACADE
// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// FILE: B.java
import java.util.List;

public class B implements X {
    @Override
    List foo(List l) {
        return super.foo(l);
    }
}

// FILE: 1.kt

interface X {
    fun foo(l: MutableList<Int>): List<String>?
}

internal class C : B()

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, interfaceDeclaration, javaType, nullableType */
