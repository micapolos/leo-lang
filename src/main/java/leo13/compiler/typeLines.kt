package leo13.compiler

import leo.base.notNullIf
import leo13.*
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.type.TypeLine
import leo13.type.booleanTypeLine

data class TypeLines(val stack: Stack<TypeLine>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = linesName lineTo stack.scripting.script.emptyIfEmpty
	fun plus(line: TypeLine) = TypeLines(stack.push(line))

	fun resolve(line: ExpressionTypedLine): ExpressionTypedLine =
		stack.mapFirst {
			notNullIf(contains(line.typeLine, null)) {
				line.name lineTo typed(line.rhs.expression, unexpandedRhs)
			}
		} ?: line
}

fun typeLines() = TypeLines(stack())

val coreTypeLines = typeLines().plus(booleanTypeLine)