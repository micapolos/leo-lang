package leo5

import leo.base.ifOrNull

data class Application(val value: Value, val line: Line)

fun application(value: Value, line: Line) = Application(value, line)
val Application.simpleLineOrNull get() = ifOrNull(value.script.isEmpty, ::line)
fun Appendable.append(application: Application) = append(application.value).append(application.line)
