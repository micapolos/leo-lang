package leo5

data class BodyApplication(val body: Body, val line: BodyLine)

fun application(body: Body, line: BodyLine) = BodyApplication(body, line)
fun BodyApplication.invoke(parameter: ValueParameter) =
	value(script(application(body.invoke(parameter), line.invoke(parameter))))
