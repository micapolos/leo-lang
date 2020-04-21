package leo15.lambda

import leo14.*

val Term.script: Script
	get() =
		null
			?: pairScriptOrNull
			//?: bindScriptOrNull
			?: when (this) {
				idTerm -> script("id")
				pairTerm -> script("pair")
				firstTerm -> script("first")
				secondTerm -> script("second")
				is ValueTerm -> script("value" lineTo script(value.toString()))
				is AbstractionTerm -> lambdaScript
				is ApplicationTerm -> lhs.script.plus("invoke" lineTo rhs.script)
				is IndexTerm -> script("get" lineTo script(literal(index)))
			}

val Term.pairScriptOrNull: Script?
	get() =
		unpairOrNull?.let { pair ->
			script(
				"pair" lineTo script(
					"first" lineTo pair.first.script,
					"second" lineTo pair.second.script))
		}

val AbstractionTerm.lambdaScript: Script
	get() =
		script("lambda" lineTo body.script)
