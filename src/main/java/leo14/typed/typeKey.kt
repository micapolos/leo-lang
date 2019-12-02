package leo14.typed

import leo14.lineTo

data class TypeKey(val type: Type) {
	override fun toString() = "$reflectScriptLine"
}

fun key(type: Type) = TypeKey(type)

val TypeKey.reflectScriptLine
	get() =
		"key" lineTo type.script

