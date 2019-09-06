package leo13.script.reflect

import leo13.fail
import leo13.script.ScriptLine
import leo13.script.lineTo

data class Field<V : Any, A : Any>(
	val type: Type<A>,
	val getFn: V.() -> A)

fun <V : Any, A : Any> field(type: Type<A>, getFn: V.() -> A) = Field(type, getFn)

fun <V : Any, A : Any> Field<V, A>.scriptLine(v: V): ScriptLine =
	type.scriptLine(v.getFn())

fun <V : Any, A : Any> Field<V, A>.unsafeValue(scriptLine: ScriptLine): A =
	if (type.name != scriptLine.name) fail("expected" lineTo leo13.script.script(type.name))
	else type.unsafeBodyValue(scriptLine.rhs)

