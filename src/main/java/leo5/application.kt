package leo5

data class Application(val value: Value, val line: ValueLine)

fun application(value: Value, line: ValueLine) = Application(value, line)
val Application.onlyLine get() = value.script.empty.run { line }
fun Appendable.append(application: Application) = append(application.value).append(application.line)
