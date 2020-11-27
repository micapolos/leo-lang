package leo23.term

import leo14.Script
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo23.term.type.scriptLine

val Expr.script: Script
	get() =
		term.script.plus("of" lineTo script(type.scriptLine))

val Term.script: Script
	get() =
		when (this) {
			NilTerm -> script("nil")
			is BooleanTerm -> script("boolean" lineTo script(if (boolean) "true" else "false"))
			is StringTerm -> script(literal(string))
			is NumberTerm -> script(literal(number))
			is IsNilTerm -> lhs.script.plus("is" lineTo script("nil"))
			is NumberPlusTerm -> lhs.script.plus("plus" lineTo rhs.script)
			is NumberMinusTerm -> lhs.script.plus("minus" lineTo rhs.script)
			is NumberTimesTerm -> lhs.script.plus("times" lineTo rhs.script)
			is NumberEqualsTerm -> lhs.script.plus("equals" lineTo rhs.script)
			is NumberStringTerm -> number.script.plus("number" lineTo script("text" lineTo script()))
			is StringAppendTerm -> lhs.script.plus("text" lineTo script("plus" lineTo rhs.script))
			is StringEqualsTerm -> lhs.script.plus("text" lineTo script("equals" lineTo rhs.script))
			is StringNumberOrNilTerm -> string.script.plus("text" lineTo script("number" lineTo script()))
			is TupleTerm -> script("vector" lineTo script(*list.map { "item" lineTo it.script }.toTypedArray()))
			is TupleAtTerm -> vector.script.plus("at" lineTo script(literal(index)))
			is ConditionalTerm -> cond.script.plus("if" lineTo script("true" lineTo caseTrue.script, "false" lineTo caseFalse.script))
			is FunctionTerm -> script("function" lineTo paramTypes.map { it.scriptLine }.script.plus("does" lineTo body.script))
			is RecursiveFunctionTerm -> script("function" lineTo paramTypes.map { it.scriptLine }.script.plus("does" lineTo script("recursively" lineTo body.script)))
			is ApplyTerm -> function.script.plus("apply" lineTo script(*paramList.map { "item" lineTo it.script }.toTypedArray()))
			is VariableTerm -> script("arg" lineTo script(literal(index)))
			is IndexedTerm -> script(line(literal(index)), "indexed" lineTo rhs.script)
			is SwitchTerm -> lhs.script.plus("switch" lineTo cases.map { "case" lineTo it.script }.script)
		}