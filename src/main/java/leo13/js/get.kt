package leo13.js

data class Get(val lhs: Expression, val name: String)

fun Expression.get(name: String) = Get(this, name)

val Get.code get() = "${lhs.code}.$name"
