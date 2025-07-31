// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

interface ProducerConsumer<T> {
    fun produce(): T
    fun consume(t: T)
}

fun process(pc: ProducerConsumer<in Pos>) = pc.consume(42 as Pos)

fun main() {
    val pc = object : ProducerConsumer<Int> {
        override fun produce(): Int = 42
        override fun consume(t: Int) {}
    }
    process(pc)
}

/* GENERATED_FIR_TAGS: functionDeclaration, interfaceDeclaration, lambdaLiteral, nullableType, outProjection,
typeParameter */
