package leo13.compiler

import leo13.ObjectScripting
import leo13.compiledName
import leo13.type.TypeArrow
import leo13.script.lineTo
import leo13.script.script
import leo13.value.ValueFunction
import leo13.value.scriptLine

data class FunctionCompiled(val function: ValueFunction, val arrow: TypeArrow) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			compiledName lineTo script(function.scriptLine, arrow.scriptingLine)
}

fun compiled(function: ValueFunction, arrow: TypeArrow) = FunctionCompiled(function, arrow)
