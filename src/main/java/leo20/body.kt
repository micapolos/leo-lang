package leo20

import leo14.Script

sealed class Body
data class ScriptBody(val script: Script) : Body()
object NumberPlusBody : Body()
object NumberMinusBody : Body()
object NumberEqualsBody : Body()

fun body(script: Script): Body = ScriptBody(script)

fun Bindings.unsafeValue(body: Body): Value =
	when (body) {
		is ScriptBody -> value(body.script)
		NumberPlusBody -> unsafeValueAt(1).unsafeNumberPlus(unsafeValueAt(0).unsafeGet("number"))
		NumberMinusBody -> unsafeValueAt(1).unsafeNumberMinus(unsafeValueAt(0).unsafeGet("number"))
		NumberEqualsBody -> unsafeValueAt(1).equals(unsafeValueAt(0).unsafeGet("number")).value
	}