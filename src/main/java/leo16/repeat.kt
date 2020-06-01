package leo16

import leo16.names.*

data class Repeat(val compiled: Compiled) {
	override fun toString() = asSentence.toString()
}

val Repeat.asSentence get() = _repeat(compiled.bodyValue)
val Compiled.repeat get() = Repeat(this)

inline fun Repeat.apply(arg: Value): Value? =
	arg.matchPrefix(_repeat) { rhs ->
		compiled.invoke(rhs)
	}
