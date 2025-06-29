// RUN_PIPELINE_TILL: FRONTEND
// FILE: MyRunnable.java
public interface MyRunnable {
    void bar();
}

// FILE: DerivedRunnable.java
public interface DerivedRunnable extends MyRunnable {
    boolean foo(int x);
}

// FILE: JavaUsage.java

public class JavaUsage {
    public static void foo(DerivedRunnable x) {}
}
// FILE: main.kt

fun foo(m: MyRunnable) {}

fun main() {
    JavaUsage.foo <!ARGUMENT_TYPE_MISMATCH!>{
            <!CANNOT_INFER_VALUE_PARAMETER_TYPE!>x<!> -> x <!UNRESOLVED_REFERENCE!>><!> 1
    }<!>

    JavaUsage.foo(<!ARGUMENT_TYPE_MISMATCH!>{ <!UNRESOLVED_REFERENCE!>it<!> > 1 }<!>)

    val x = { x: Int -> x > 1 }

    JavaUsage.foo(<!ARGUMENT_TYPE_MISMATCH!>x<!>)
}

/* GENERATED_FIR_TAGS: comparisonExpression, functionDeclaration, integerLiteral, javaFunction, javaType, lambdaLiteral,
localProperty, propertyDeclaration */
