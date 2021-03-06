package leo13.compiler

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.type.TypeArrow
import leo13.typedName
import leo13.value.ValueFunction
import leo13.value.scriptLine

data class FunctionTyped(
	val function: ValueFunction,
	val arrow: TypeArrow,
	val recursive: BooleanRecursive) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			typedName lineTo script(
				function.scriptLine,
				arrow.scriptingLine,
				recursive.scriptingLine)
}

fun typed(function: ValueFunction, arrow: TypeArrow, recursive: BooleanRecursive) =
	FunctionTyped(function, arrow, recursive)
