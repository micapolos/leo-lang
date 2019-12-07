package leo14.lambda.js.expr

import leo13.int
import leo14.code.Code
import leo14.code.code
import leo14.js.ast.expr
import leo14.js.ast.id
import leo14.js.ast.invoke
import leo14.js.ast.lambda
import leo14.lambda.*
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.code.inc
import leo14.js.ast.Expr as AstExpr

val Term<Expr>.code: Code
	get() =
		code(gen)

fun Term<Expr>.code(gen: Gen): Code =
	when (this) {
		is NativeTerm -> native.code(gen)
		is AbstractionTerm -> abstraction.code(gen)
		is ApplicationTerm -> application.code(gen)
		is VariableTerm -> variable.code(gen)
	}

fun Abstraction<Term<Expr>>.code(gen: Gen): Code =
	"${gen.depth.variableCode}=>${gen.inc { body.code(it) }}".code

fun Application<Term<Expr>>.code(gen: Gen): Code =
	"(${lhs.code(gen)})(${rhs.code(gen)})".code

fun Variable<Expr>.code(gen: Gen): Code =
	gen.depth.minus(index.int + 1).variableCode

val Int.variableCode: Code
	get() =
		"v$this".code

// --- AST

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
	expr(id(gen.depth.minus(index.int + 1).variableCode.string))

