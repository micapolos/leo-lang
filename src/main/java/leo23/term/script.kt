package leo23.term

import leo14.Script
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script

val Term.script: Script
	get() =
		when (this) {
			NilTerm -> script("nil")
			is BooleanTerm -> script("boolean" lineTo script(if (boolean) "true" else "false"))
			is StringTerm -> script(literal(string))
			is NumberTerm -> script(literal(number))
			is IsNilTerm -> lhs.script.plus("is" lineTo script("nil"))
			is PlusTerm -> lhs.script.plus("plus" lineTo rhs.script)
			is MinusTerm -> lhs.script.plus("minus" lineTo rhs.script)
			is TimesTerm -> lhs.script.plus("times" lineTo rhs.script)
			is EqualsTerm -> lhs.script.plus("equals" lineTo rhs.script)
			is NumberStringTerm -> lhs.script.plus("number" lineTo script("text" lineTo script()))
			is StringAppendTerm -> lhs.script.plus("text" lineTo script("plus" lineTo rhs.script))
			is StringEqualsTerm -> lhs.script.plus("text" lineTo script("equals" lineTo rhs.script))
			is StringNumberOrNilTerm -> lhs.script.plus("text" lineTo script("number" lineTo rhs.script))
			is PairTerm -> lhs.script.plus("to" lineTo rhs.script)
			is LhsTerm -> pair.script.plus("lhs" lineTo script())
			is RhsTerm -> pair.script.plus("rhs" lineTo script())
			is VectorTerm -> script("vector" lineTo script(*list.map { "item" lineTo it.script }.toTypedArray()))
			is VectorAtTerm -> vector.script.plus("at" lineTo index.script)
			is ConditionalTerm -> cond.script.plus("if" lineTo script("true" lineTo caseTrue.script, "false" lineTo caseFalse.script))
			is FunctionTerm -> script("function" lineTo script("arity" lineTo script(literal(arity))).plus(body.script))
			is ApplyTerm -> function.script.plus("apply" lineTo script(*paramList.map { "item" lineTo it.script }.toTypedArray()))
			is VariableTerm -> script("arg" lineTo script(literal(index)))
		}