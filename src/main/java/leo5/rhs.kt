package leo5

data class Rhs(val body: Body)

fun rhs(body: Body) = Rhs(body)
fun Rhs.invoke(parameter: ValueParameter) = body.invoke(parameter).rhs
