package leo16

import leo16.names.*

data class Repeat(val compiled: Compiled) {
	override fun toString() = asField.toString()
}

val Repeat.asField get() = _recursing(compiled.bodyValue)
val Compiled.repeat get() = Repeat(this)

inline fun Repeat.apply(arg: Value): Value? =
	arg.matchPrefix(_containing) { rhs ->
		compiled.invoke(rhs)
	}
