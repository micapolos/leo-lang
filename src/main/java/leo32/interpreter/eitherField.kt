package leo32.interpreter

import leo32.runtime.to

data class EitherField(
	val name: String,
	val value: Kind)

infix fun String.to(value: Kind) =
	EitherField(this, value)

val EitherField.termField get() =
	name to value.term