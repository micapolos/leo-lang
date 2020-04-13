package leo14.lambda2

import leo14.Script
import leo14.lineTo
import leo14.plus
import leo14.script

val Term.script: Script
	get() =
		null
			?: pairScriptOrNull
			?: when (this) {
				id -> script("id")
				pair -> script("pair")
				first -> script("first")
				second -> script("second")
				is ValueTerm -> script("value" lineTo script(value.toString()))
				is AbstractionTerm -> script("lambda" lineTo body.script)
				is ApplicationTerm -> lhs.script.plus("invoke" lineTo rhs.script)
				is IndexTerm -> script("get" lineTo script("$index"))
			}

val Term.pairScriptOrNull: Script?
	get() =
		unpairOrNull?.let { pair ->
			script(
				"pair" lineTo script(
					"first" lineTo pair.first.script,
					"second" lineTo pair.second.script))
		}