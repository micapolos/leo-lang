package leo13.untyped.expression

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script

data class Wrap(val name: String) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "wrap" lineTo script(name)
}

fun wrap(name: String) = Wrap(name)