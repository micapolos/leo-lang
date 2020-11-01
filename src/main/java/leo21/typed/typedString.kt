package leo21.typed

import leo.base.failIfOr
import leo13.EmptyStack
import leo13.LinkStack
import leo14.Script
import leo14.ScriptLine
import leo14.lambda.Term
import leo14.lambda.native
import leo14.lambda.pair
import leo14.lambda.value.Value
import leo14.lambda.value.double
import leo14.lambda.value.string
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo21.term.nilTerm
import leo21.type.DoubleLine
import leo21.type.Field
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.StringLine
import leo21.type.Type
import leo21.type.type

val Typed.script: Script
	get() =
		script(valueTerm, type)

fun script(valueTerm: Term<Value>, type: Type): Script =
	when (type.lineStack) {
		is EmptyStack -> failIfOr(valueTerm != nilTerm) { script() }
		is LinkStack -> valueTerm.pair().let { (lhs, rhs) ->
			script(lhs, type.lineStack.link.stack.type).plus(scriptLine(rhs, type.lineStack.link.value))
		}
	}

fun scriptLine(value: Term<Value>, line: Line): ScriptLine =
	when (line) {
		StringLine -> leo14.line(literal(value.native.string))
		DoubleLine -> leo14.line(literal(value.native.double))
		is FieldLine -> scriptLine(value, line.field)
	}

fun scriptLine(value: Term<Value>, field: Field): ScriptLine =
	field.name lineTo script(value, field.rhs)
