package leo13.type

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.toName

data class TypeTo(val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = toName lineTo script(type.scriptingLine)
}

fun to(type: Type) = TypeTo(type)