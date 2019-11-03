package leo13.js.ast

data class Get(val lhs: Expr, val name: String)

fun Expr.get(name: String) = expr(Get(this, name))
val Get.exprCode get() = "(${lhs.code}).$name"