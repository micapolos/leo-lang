package leo32.runtime

data class ValueField(
	val key: Key,
	val value: Value)

infix fun Key.to(value: Value) =
	ValueField(this, value)

infix fun String.to(value: Value) =
	key(this) to value