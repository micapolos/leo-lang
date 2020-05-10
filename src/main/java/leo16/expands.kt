package leo16

import leo16.names.*

data class Expands(val pattern: Pattern, val function: Function) {
	override fun toString() = asValue.toString()
	val asValue get() = pattern.asValue.plus(_expands(function.bodyValue))
}

fun Pattern.expands(function: Function) = Expands(this, function)
