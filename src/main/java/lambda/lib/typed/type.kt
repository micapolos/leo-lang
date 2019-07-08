package lambda.lib.typed

import lambda.Term

class Type(
	val name: Name,
	val dispatch: MutableMap<Name, Term>) {
	override fun equals(other: Any?) = other is Type && name == other.name
	override fun hashCode() = name.hashCode()
}