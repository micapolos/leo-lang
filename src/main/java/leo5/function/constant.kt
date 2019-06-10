package leo5.function

data class Constant(val any: Any)

fun constant(any: Any) = Constant(any)
fun Constant.invoke(parameter: Any) = any