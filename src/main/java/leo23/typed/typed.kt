package leo23.typed

import leo14.anyReflectScriptLine
import leo14.error
import leo15.dsl.*

data class Typed<out V, out T>(val v: V, val t: T)

fun <V, T> V.of(t: T) = Typed(this, t)

fun <V, T> Typed<V, T>.v(t: T): V =
	if (this.t == t) v
	else error { expected { x(t.anyReflectScriptLine) } }
