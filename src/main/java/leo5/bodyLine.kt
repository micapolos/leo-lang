package leo5

data class BodyLine(val name: String, val body: Body)

fun line(name: String, body: Body) = BodyLine(name, body)
fun BodyLine.invoke(parameter: ValueParameter) = line(name, body.invoke(parameter))