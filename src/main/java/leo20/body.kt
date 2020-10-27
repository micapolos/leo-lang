package leo20

import leo14.Script

sealed class Body
data class ScriptBody(val script: Script) : Body()
object NumberPlusBody : Body()
object NumberMinusBody : Body()
object EqualsBody : Body()

fun body(script: Script): Body = ScriptBody(script)

fun Scope.unsafeValue(body: Body): Value =
	when (body) {
		is ScriptBody -> value(body.script)
		NumberPlusBody -> unsafeValueAt(0).run { unsafeGet("number").unsafeNumberPlus(unsafeGet("plus")) }
		NumberMinusBody -> unsafeValueAt(0).run { unsafeGet("number").unsafeNumberPlus(unsafeGet("plus")) }
		EqualsBody -> TODO()
	}