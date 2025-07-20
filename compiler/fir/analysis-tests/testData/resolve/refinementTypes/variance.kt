// FIR_IDENTICAL
// RUN_PIPELINE_TILL: BACKEND
// WITH_EXTRA_CHECKERS

refinement Pos = Int satisfies { it > 0 }

interface ProducerConsumer<T> {
    fun produce(): T
//    fun consume(t: T)
}

//fun process(pc: ProducerConsumer<out Int>) = pc.produce()

fun main() {
//    val pc = object : ProducerConsumer<Int> {
//        override fun produce(): Int = 42 as Int
//    }
    val posPc = object : ProducerConsumer<Pos> {
        override fun produce(): Pos = 42 as Pos
    }
//    process(posPc)
}

/* GENERATED_FIR_TAGS: functionDeclaration, interfaceDeclaration, lambdaLiteral, nullableType, typeParameter */
