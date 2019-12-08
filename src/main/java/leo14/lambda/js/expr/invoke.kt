package leo14.lambda.js.expr

import leo13.Stack
import leo13.fold
import leo13.map
import leo13.stack
import leo14.js.ast.Params
import leo14.js.ast.invoke
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.lambda.script
import leo14.lambda.scriptLine
import leo14.lineTo
import leo14.plus
import leo14.script
import leo14.js.ast.Expr as AstExpr

data class Invoke(val lhs: Term<Expr>, val argStack: Stack<Term<Expr>>)

fun Term<Expr>.invoke(vararg args: Term<Expr>) = Invoke(this, stack(*args))

fun Term<Expr>.invoke(argStack: Stack<Term<Expr>>) = Invoke(this, argStack)

fun Invoke.astExpr(gen: Gen): AstExpr =
	lhs.astExpr(gen).invoke(Params(argStack.map { astExpr(gen) }))

val Invoke.reflectScript
	get() =
		lhs.script(Expr::reflectScriptLine)
			.plus("invoke" lineTo script()
				.fold(argStack) {
					plus(it.scriptLine(Expr::reflectScriptLine))
				})

