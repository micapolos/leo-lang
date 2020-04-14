package leo14.lambda2

import leo14.*

val Term.script: Script
	get() =
		null
			?: pairScriptOrNull
			//?: bindScriptOrNull
			?: when (this) {
				id -> script("id")
				pair -> script("pair")
				first -> script("first")
				second -> script("second")
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
