package leo32.runtime

data class EitherField(
	val name: String,
	val value: Type)

infix fun String.to(value: Type) =
	EitherField(this, value)

val EitherField.termField get() =
	name to value.term

val TermField.parseEitherField get() =
	name to value.parseType

val TermField.rawEitherField get() =
	name to value.rawType

val EitherField.seq32 get() =
	termField.seq32
