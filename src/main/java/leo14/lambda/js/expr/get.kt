package leo14.lambda.js.expr

import leo14.code.Code
import leo14.code.code
import leo14.js.ast.get
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.js.ast.Expr as AstExpr

data class Get(val lhs: Term<Expr>, val name: String)

fun Term<Expr>.get(name: String) = Get(this, name)

fun Get.code(gen: Gen): Code =
	"(${lhs.code(gen)}).$name".code

fun Get.astExpr(gen: Gen): AstExpr =
	lhs.astExpr(gen).get(name)