package leo16

import leo16.names.*

data class Lazy(val compiled: Compiled) {
	override fun toString() = asField.toString()
	val asField get() = _lazy(compiled.bodyValue)
	val evaluate: Value get() = compiled.evaluate
}

val Compiled.lazy get() = Lazy(this)