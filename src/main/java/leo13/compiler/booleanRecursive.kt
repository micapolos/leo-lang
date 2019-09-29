package leo13.compiler

import leo13.ObjectScripting
import leo13.recursiveName
import leo13.script.lineTo
import leo13.script.script

data class BooleanRecursive(val boolean: Boolean) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = recursiveName lineTo script("$boolean")
}

fun recursive(boolean: Boolean) = BooleanRecursive(boolean)