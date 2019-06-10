package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Script

data class EmptyScript(val empty: Empty) : Script()
data class ApplicationScript(val application: Application) : Script()

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(application: Application): Script = ApplicationScript(application)

val Script.isEmpty get() = this is EmptyScript
val Script.applicationOrNull get() = (this as? ApplicationScript)?.application

fun Script.apply(line: ValueLine) = script(application(value(this), line))
fun Script.apply(vararg lines: ValueLine) = fold(lines) { apply(it) }
fun script(vararg lines: ValueLine) = script(empty).apply(*lines)

fun Appendable.append(script: Script): Appendable = when (script) {
	is EmptyScript -> this
	is ApplicationScript -> append(script.application)
}
