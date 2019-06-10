package leo5

data class Application(val value: Value, val line: Line)

fun application(value: Value, line: Line) = Application(value, line)
fun Appendable.append(application: Application) = append(application.value).append(application.line)
