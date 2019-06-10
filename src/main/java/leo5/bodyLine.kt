package leo5

data class BodyLine(val name: String, val body: Body)

fun line(name: String, body: Body) = BodyLine(name, body)
infix fun String.lineTo(body: Body) = line(this, body)

fun BodyLine.invoke(parameter: ValueParameter) = line(name, body.invoke(parameter))