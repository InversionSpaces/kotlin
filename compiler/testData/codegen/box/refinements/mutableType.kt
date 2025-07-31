// TARGET_BACKEND: JVM
// WITH_STDLIB

// TODO: Prohibit mutable underlying types
refinement NonEmptyIntList = List<Int> satisfies { it.isNotEmpty() }

fun test(neil: NonEmptyIntList): String =
    if (neil.isNotEmpty()) "OK" else "NOT OK"

// TEST FAILS
fun box(): String {
    val l = mutableListOf(1, 2, 3)
    val neil = l as NonEmptyIntList
    l.removeAll { true }
    return test(neil)
}