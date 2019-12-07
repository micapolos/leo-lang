package leo14.lambda.js.expr

import leo14.*
import leo14.js.ast.get
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.lambda.script
import leo14.js.ast.Expr as AstExpr

data class Get(val lhs: Term<Expr>, val name: String)

fun Term<Expr>.get(name: String) = Get(this, name)

fun Get.astExpr(gen: Gen): AstExpr =
	lhs.astExpr(gen).get(name)

val Get.reflectScript: Script
	get() =
		lhs
			.script(Expr::reflectScriptLine)
			.plus("get" lineTo script(literal(name)))