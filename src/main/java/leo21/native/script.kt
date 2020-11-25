package leo21.native

import leo14.Script
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script

val Native.script: Script
	get() =
		when (this) {
			NilNative -> script("nil")
			is BooleanNative -> script("boolean" lineTo script(if (boolean) "true" else "false"))
			is StringNative -> script(literal(string))
			is NumberNative -> script(literal(number))
			is IsNilNative -> lhs.script.plus("is" lineTo script("nil"))
			is PlusNative -> lhs.script.plus("plus" lineTo rhs.script)
			is MinusNative -> lhs.script.plus("minus" lineTo rhs.script)
			is TimesNative -> lhs.script.plus("times" lineTo rhs.script)
			is EqualsNative -> lhs.script.plus("equals" lineTo rhs.script)
			is NumberStringNative -> lhs.script.plus("number" lineTo script("text" lineTo script()))
			is StringAppendNative -> lhs.script.plus("text" lineTo script("plus" lineTo rhs.script))
			is StringEqualsNative -> lhs.script.plus("text" lineTo script("equals" lineTo rhs.script))
			is StringNumberOrNilNative -> lhs.script.plus("text" lineTo script("number" lineTo rhs.script))
			is PairNative -> lhs.script.plus("to" lineTo rhs.script)
			is LhsNative -> pair.script.plus("lhs" lineTo script())
			is RhsNative -> pair.script.plus("rhs" lineTo script())
			is VectorNative -> script("vector" lineTo script(*list.map { "item" lineTo it.script }.toTypedArray()))
			is VectorAtNative -> vector.script.plus("at" lineTo index.script)
			is ConditionalNative -> cond.script.plus("if" lineTo script("true" lineTo caseTrue.script, "false" lineTo caseFalse.script))
			is FunctionNative -> script("function" lineTo script("arity" lineTo script(literal(arity))).plus(body.script))
			is ApplyNative -> function.script.plus("apply" lineTo script(*paramList.map { "item" lineTo it.script }.toTypedArray()))
			is VariableNative -> script("arg" lineTo script(literal(index)))
		}