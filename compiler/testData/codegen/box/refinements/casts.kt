// TARGET_BACKEND: JVM
// WITH_STDLIB

refinement Pos = Int satisfies { it > 0 }
refinement PosEven = Pos satisfies { it % 2 == 0 }

fun box(): String {
    val valid = listOf(2, 4, 6)
    val invalid = listOf(-2, -1, 0, 3)

    for (i in valid) {
        if (i is PosEven) {} else return "Error on `is`: $i"
        if (i !is PosEven) return "Error on `!is`: $i"
        val t1 = (i as? PosEven) ?: return "Error on `as?`: $i"
        try {
            val t2 = i as PosEven
        } catch (e: TypeCastException) {
            return "Error on `as`: $i"
        }
    }

    for (i in invalid) {
        if (i is PosEven) return "Error on `is`: $i"
        if (i !is PosEven) {} else return "Error on `!is`: $i"
        val t1 = (i as? PosEven)?.let { return "Error on `as?`: $i" }
        try {
            val t2 = i as PosEven
            return "Error on `as`: $i"
        } catch (e: TypeCastException) {}
    }

    return "OK"
}