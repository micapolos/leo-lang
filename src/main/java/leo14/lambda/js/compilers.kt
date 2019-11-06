package leo14.lambda.js

import leo14.*
import leo14.js.ast.Expr
import leo14.lambda.termCompiler

fun exprCompiler(ret: (Expr) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken -> ret(token.literal.expr)
			else -> error("expected js expr")
		}
	}

val compileExpr: Compile<Expr> = { exprCompiler(it) }

val compiler
	get() =
		termCompiler(
			compileError("fallback"),
			compileExpr,
			ret())
