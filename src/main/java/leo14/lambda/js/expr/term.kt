package leo14.lambda.js.expr

import leo13.int
import leo14.code.Code
import leo14.code.code
import leo14.js.ast.*
import leo14.lambda.*
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.code.inc
import leo14.js.ast.Expr as AstExpr

val Term<Expr>.code: Code
	get() =
		astExpr.code.code

// --- AST

val Term<Expr>.astExpr: AstExpr
	get() =
		astExpr(gen)

fun Term<Expr>.astExpr(gen: Gen): AstExpr =
	when (this) {
		is NativeTerm -> native.astExpr(gen)
		is AbstractionTerm -> abstraction.astExpr(gen)
		is ApplicationTerm -> application.astExpr(gen)
		is VariableTerm -> variable.astExpr(gen)
	}

fun Abstraction<Term<Expr>>.astExpr(gen: Gen): AstExpr =
	expr(lambda(gen.depth.variableCode.string, gen.inc { body.astExpr(it) }))

fun Application<Term<Expr>>.astExpr(gen: Gen): AstExpr =
	lhs.astExpr(gen).invoke(rhs.astExpr(gen))

fun Variable<Expr>.astExpr(gen: Gen): AstExpr =
	gen.depth.minus(index.int + 1).variableAstExpr

val Int.variableAstExpr
	get() =
		expr(id(variableCode.string))

val Int.variableCode
	get() =
		"v$this".code