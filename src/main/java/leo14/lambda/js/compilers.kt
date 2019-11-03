package leo14.lambda.js

import leo13.js.ast.Expr
import leo13.js.ast.expr
import leo14.*
import leo14.lambda.valueCompiler

fun exprCompiler(ret: (Expr) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is StringToken -> ret(expr(token.string))
			is NumberToken -> ret(token.number.expr)
			else -> error("expected js expr")
		}
	}

val compileExpr: Compile<Expr> = { exprCompiler(it) }

val compiler
	get() =
		valueCompiler(
			compileError("fallback"),
			compileExpr,
			ret())
