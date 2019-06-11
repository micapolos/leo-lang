package leo5

import leo.base.notNullIf

data class Application(val value: Value, val line: ValueLine)

fun application(value: Value, line: ValueLine) = Application(value, line)
val Application.onlyLineOrNull get() = notNullIf(value.isEmpty) { line }
val Application.onlyLine get() = onlyLineOrNull!!
fun Appendable.append(application: Application) = append(application.value).append(application.line)
