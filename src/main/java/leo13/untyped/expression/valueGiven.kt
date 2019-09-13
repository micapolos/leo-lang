package leo13.untyped.expression

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.givenName
import leo13.untyped.value.Value
import leo13.untyped.value.ValueLine
import leo13.untyped.value.lineTo
import leo13.untyped.value.scriptLine
import leo9.Stack
import leo9.push
import leo9.stack

data class ValuesGiven(val valueStack: Stack<Value>)

val Stack<Value>.given get() = ValuesGiven(this)
fun given(value: Value, vararg values: Value) = ValuesGiven(stack(value, *values))
fun givenValues() = ValuesGiven(stack())

fun ValuesGiven.plus(value: Value) =
	valueStack.push(value).given

fun ValuesGiven.evaluate(expression: Expression): Value =
	evaluator().plus(expression).value

fun ValuesGiven.evaluate(line: ExpressionLine): ValueLine =
	line.name lineTo evaluate(line.rhs)

val ValuesGiven.scriptLine: ScriptLine
	get() =
		givenName lineTo valueStack.script { scriptLine }
