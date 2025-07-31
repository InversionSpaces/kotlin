// TARGET_BACKEND: JVM
// WITH_STDLIB

refinement Pos = Int satisfies { it > 0 }

// TODO: Fix reified types
inline fun <reified T> test(x: Int): T {
    return x as T
}

// TEST FAILS
fun box(): String {
    try {
        val x = test<Pos>(-1)
        return "NOT OK"
    } catch (e: TypeCastException) {
        return "OK"
    }
}