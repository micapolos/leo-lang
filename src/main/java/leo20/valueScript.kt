package leo20

import leo.base.fold
import leo13.reverse
import leo13.seq
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.literal
import leo14.number
import leo14.plus
import leo14.script
import java.math.BigDecimal

val Value.script: Script
	get() =
		script().fold(lineStack.reverse.seq) { this.plus(it.scriptLine) }

val Line.scriptLine: ScriptLine
	get() =
		when (this) {
			is FieldLine -> field.scriptLine
			is StringLine -> leo14.line(literal(string))
			is NumberLine -> leo14.line(literal(number(BigDecimal(number.toString()))))
			is FunctionLine -> "function" lineTo function.body.script
		}

val Field.scriptLine
	get() =
		name lineTo rhs.script

val Body.script
	get() =
		when (this) {
			is ScriptBody -> script
			NumberPlusBody -> script(toString())
			NumberMinusBody -> script(toString())
			NumberEqualsBody -> script(toString())
		}