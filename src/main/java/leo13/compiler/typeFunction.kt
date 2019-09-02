package leo13.compiler

import leo13.LeoObject
import leo13.script.lineTo
import leo13.script.script
import leo13.type.Type

// TODO: This should be just typeLine.
data class TypeFunction(val name: String, val type: Type) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "function"
	override val scriptableBody get() = script("name" lineTo script(name), type.scriptableLine)
}

fun function(name: String, type: Type) = TypeFunction(name, type)
