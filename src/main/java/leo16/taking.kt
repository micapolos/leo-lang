package leo16

import leo.base.notNullIf
import leo15.takingName

data class Taking(val pattern: Pattern, val function: Function) {
	override fun toString() = asField.toString()
}

fun Pattern.giving(function: Function) = Taking(this, function)

fun Taking.take(value: Value): Value? =
	notNullIf(value.matches(pattern)) {
		function.invoke(value)
	}

val Taking.asField: Field
	get() =
		takingName(pattern.asField, function.asField)
