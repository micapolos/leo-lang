package leo13.js2

data class Get(val lhs: Expr, val name: String)

fun Expr.get(name: String) = expr(Get(this, name))
val Get.code get() = "${lhs.code}.$name"