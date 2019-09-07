package leo13.scripter

import leo13.fail
import leo13.script.ScriptLine
import leo13.script.lineTo

data class Field<V : Any, A : Any>(
	val scripter: Scripter<A>,
	val getFn: V.() -> A)

fun <V : Any, A : Any> field(type: Scripter<A>, getFn: V.() -> A) = Field(type, getFn)

fun <V : Any, A : Any> Field<V, A>.scriptLine(v: V): ScriptLine =
	scripter.scriptLine(v.getFn())

fun <V : Any, A : Any> Field<V, A>.unsafeValue(scriptLine: ScriptLine): A =
	if (scripter.name != scriptLine.name) fail("expected" lineTo leo13.script.script(scripter.name))
	else scripter.unsafeBodyValue(scriptLine.rhs)

