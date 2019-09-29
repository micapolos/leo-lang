package leo13.type

import leo13.ObjectScripting
import leo13.ofName
import leo13.script.lineTo
import leo13.script.script

data class TypeOf(val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = ofName lineTo script(type.scriptingLine)
}

fun of(type: Type) = TypeOf(type)