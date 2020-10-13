package leo19.typed

import leo14.lineTo
import leo14.untyped.pretty.indentString
import leo19.type.caseTo
import leo19.type.fieldTo

data class TypedField(val name: String, val typed: Typed) {
	override fun toString() = reflectScriptLine.indentString(0)
}

infix fun String.fieldTo(typed: Typed) = TypedField(this, typed)

val TypedField.reflectScriptLine get() = name lineTo typed.reflectScript

val TypedField.typeField get() = name fieldTo typed.type
val TypedField.typeCase get() = name caseTo typed.type
