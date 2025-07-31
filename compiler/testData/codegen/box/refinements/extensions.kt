// TARGET_BACKEND: JVM
// WITH_STDLIB

refinement Pos = Int satisfies { it > 0 }

fun Int.test(): String = "NOT OK"
// TODO: Fix signature generation with refinement types?
@JvmName("testPos")
fun Pos.test(): String = "OK"

fun pos(): Pos = 42 as Pos

fun box(): String {
    return pos().test()
}