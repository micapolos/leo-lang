package leo5

data class Lhs(val body: Body)

fun lhs(body: Body) = Lhs(body)
fun Lhs.invoke(parameter: ValueParameter) = body.invoke(parameter).invokeLhs
