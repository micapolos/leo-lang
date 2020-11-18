package leo14

import leo15.dsl.*

data class ScriptError(val script: Script) : Error()

val Script.error: Nothing get() = throw ScriptError(this)
fun error(vararg lines: ScriptLine): Nothing = script(*lines).error
fun error(f: F): Nothing = script_(f).error
fun <R> R?.orError(f: F) = this ?: error(f)
fun <R> R?.orError(vararg lines: ScriptLine) = this ?: error(*lines)
