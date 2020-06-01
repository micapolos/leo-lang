package leo16

data class Type(val value: Value)

val Value.type get() = Type(this)