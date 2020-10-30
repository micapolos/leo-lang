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

val Value.script: Script
	get() =
		script().fold(lineStack.reverse.seq) { this.plus(it.scriptLine) }

val Line.scriptLine: ScriptLine
	get() =
		null
			?: bigDecimalOrNull?.run { leo14.line(literal(number(this))) }
			?: stringOrNull?.run { leo14.line(literal(this)) }
			?: when (this) {
				is FieldLine -> field.scriptLine
				is NativeLine -> "native" lineTo script(native.toString())
				is FunctionLine -> "function" lineTo function.body.script
			}

val Field.scriptLine
	get() =
		null
			?: name lineTo rhs.script

val Body.script
	get() =
		when (this) {
			is ScriptBody -> script
			NumberPlusBody -> script(toString())
			NumberMinusBody -> script(toString())
			NumberEqualsBody -> script(toString())
		}