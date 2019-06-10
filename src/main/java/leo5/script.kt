package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Script

data class EmptyScript(val empty: Empty) : Script()
data class ApplicationScript(val application: Application) : Script()

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(application: Application): Script = ApplicationScript(application)

val Script.emptyOrNull get() = (this as? EmptyScript)?.empty
val Script.applicationOrNull get() = (this as? ApplicationScript)?.application

fun Script.plus(vararg lines: Line) = fold(lines) { script(application(value(this), it)) }
fun script(vararg lines: Line) = script(empty).plus(*lines)

fun Appendable.append(script: Script): Appendable = when (script) {
	is EmptyScript -> this
	is ApplicationScript -> append(script.application)
}
