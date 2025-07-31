// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

interface ProducerConsumer<T> {
    fun produce(): T
    fun consume(t: T)
}

fun process(pc: ProducerConsumer<out Int>) = pc.produce()

fun main() {
    val posPc = object : ProducerConsumer<Pos> {
        override fun produce(): Pos = 42 as Pos
        override fun consume(t: Pos) {}
    }
    process(posPc)
}

/* GENERATED_FIR_TAGS: functionDeclaration, interfaceDeclaration, lambdaLiteral, nullableType, outProjection,
typeParameter */
