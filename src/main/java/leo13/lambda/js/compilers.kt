package leo13.lambda.js

import leo13.js.ast.Expr
import leo13.js.ast.expr
import leo13.js.compiler.*

fun exprCompiler(ret: (Expr) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is StringToken -> ret(expr(token.string))
			is NumberToken -> ret(token.number.expr)
			else -> error("expected js expr")
		}
	}

val compileExpr: Compile<Expr> = { exprCompiler(it) }