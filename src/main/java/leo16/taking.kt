package leo16

import leo.base.notNullIf
import leo16.names.*

data class Taking(val pattern: Pattern, val function: Function) {
	override fun toString() = asField.toString()
}

fun Pattern.giving(function: Function) = Taking(this, function)

fun Taking.take(value: Value): Value? =
	notNullIf(value.matches(pattern)) {
		// TODO: any
		function.invoke(value.match)
	}

val Taking.asField: Field
	get() =
		_taking(pattern.asField, function.asField)
