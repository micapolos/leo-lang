package leo14

import leo15.dsl.*

data class ScriptError(val script: Script) : Error()

val Script.error: Nothing get() = throw ScriptError(this)
fun error(vararg lines: ScriptLine): Nothing = script(*lines).error
fun error(f: F): Nothing = script_(f).error

fun <R> R?.orError(script: Script) = this ?: script.error
fun <R> R?.orError(vararg lines: ScriptLine) = orError(script(*lines))
fun <R> R?.orError(f: F) = orError(script_(f))
