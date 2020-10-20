package leo19.term

import leo.base.runIf
import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script

val Term.reflect: ScriptLine
	get() =
		"term" lineTo reflectScript

val Term.reflectScript: Script
	get() =
		when (this) {
			NullTerm -> script("null")
			is IntTerm -> script(literal(int))
			is ArrayTerm -> script(*stack.map { "push" lineTo reflectScript }.array)
			is ArrayGetTerm -> tuple.reflectScript.plus("get" lineTo index.reflectScript)
			is FunctionTerm -> script(function.reflectScriptLine)
			is InvokeTerm -> function.reflectScript.plus("invoke" lineTo param.reflectScript)
			is VariableTerm -> script(variable.reflectScriptLine)
			is EqualsTerm -> lhs.reflectScript.plus("equals" lineTo rhs.reflectScript)
			is PairTerm -> lhs.reflectScript.plus("to" lineTo rhs.reflectScript)
			is LhsTerm -> pair.reflectScript.plus("lhs" lineTo script())
			is RhsTerm -> pair.reflectScript.plus("rhs" lineTo script())
		}

val Function.reflectScriptLine
	get() =
		("function" lineTo body.reflectScript).runIf(isRecursive) {
			"recursive" lineTo script(this)
		}

val Variable.reflectScriptLine: ScriptLine
	get() =
		"variable" lineTo script(literal(index))
