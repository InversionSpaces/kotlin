// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// LANGUAGE: +MultiPlatformProjects
// DIAGNOSTICS: -UNUSED_VARIABLE

// MODULE: m1
// FILE: a.kt

package p

public class A {
    internal val a = A()
    internal var v = A()
    internal fun a() = A()
    internal inner class B
}

internal val a = A()
internal var v = A()
internal fun a() = A()
internal class B

// MODULE: m2()()(m1)
// FILE: b.kt

import p.*

fun test2() {
    val _a = a
    val _v = v
    a()
    B()

    val inst = A()
    val ia = inst.a
    val iv = inst.v
    inst.a()
    inst.B()
}


// MODULE: m3()()(m2)
// FILE: c.kt

import p.*

fun test3() {
    val _a = a
    val _v = v
    a()
    B()

    val inst = A()
    val ia = inst.a
    val iv = inst.v
    inst.a()
    inst.B()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, inner, localProperty, propertyDeclaration */
