package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.lineTo
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.pattern.lineTo

data class CompiledLine(val name: String, val rhs: Compiled) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "line" lineTo script(name lineTo script(rhs.scriptingLine))
}

infix fun String.lineTo(rhs: Compiled) = CompiledLine(this, rhs)

val CompiledLine.op get() = plus(name lineTo rhs.expression).op
val CompiledLine.expressionLine get() = name lineTo rhs.expression
val CompiledLine.patternLine get() = name lineTo rhs.pattern
