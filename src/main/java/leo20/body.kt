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
		NumberPlusBody -> unsafeValueAt(1).unsafeNumberPlus(unsafeValueAt(0).unsafeGet("number"))
		NumberMinusBody -> unsafeValueAt(1).unsafeNumberMinus(unsafeValueAt(0).unsafeGet("number"))
		EqualsBody -> TODO()
	}