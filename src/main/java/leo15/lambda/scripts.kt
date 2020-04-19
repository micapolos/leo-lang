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
				is RepeatTerm -> script("repeat" lineTo rhs.script)
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
		script(isRepeating.isRepeatingLambdaName lineTo body.script)

val Boolean.isRepeatingLambdaName: String
	get() =
		if (this) "repeating" else "lambda"

//val Term.bindScriptOrNull: Script?
//	get() =
//		bindScriptOrNull(stack())
//
//fun Term.bindScriptOrNull(args: Stack<Term>): Script? =
//	when (this) {
//		is ValueTerm -> null
//		is AbstractionTerm -> when (args) {
//			is EmptyStack -> lambdaScript
//			is LinkStack -> body.bindScriptOrNull(args.link.stack)?.let { rhsScript ->
//				script("bind" lineTo args.link.value.script).plus(rhsScript)
//			}
//		}
//		is ApplicationTerm -> lhs.bindScriptOrNull(args.push(rhs))
//		is IndexTerm -> null
//	}
