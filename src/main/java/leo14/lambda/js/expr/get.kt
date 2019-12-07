package leo14.lambda.js.expr

import leo14.ScriptLine
import leo14.js.ast.get
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.lambda.scriptLine
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.js.ast.Expr as AstExpr

data class Get(val lhs: Term<Expr>, val name: String)

fun Term<Expr>.get(name: String) = Get(this, name)

fun Get.astExpr(gen: Gen): AstExpr =
	lhs.astExpr(gen).get(name)

val Get.reflectScriptLine: ScriptLine
	get() =
		"get" lineTo script(
			"lhs" lineTo script(lhs.scriptLine(Expr::reflectScriptLine)),
			"name" lineTo script(literal(name)))