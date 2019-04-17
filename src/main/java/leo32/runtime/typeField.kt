package leo32.runtime

import leo.base.empty
import leo.base.string
import leo.base.then
import leo32.seq32

data class TypeField(
	val name: Symbol,
	val value: Type) {
	override fun toString() = termField.string
}

infix fun Symbol.to(value: Type) =
	TypeField(this, value)

infix fun String.to(value: Type) =
	symbol(this) to value

fun typeField(string: String) =
	string to empty.type

val TypeField.seq32 get() =
	name.string.seq32.then { value.seq32 }

val TypeField.termField get() =
	name to value.term

val TermField.typeField: TypeField
	get() =
	name to value.type
