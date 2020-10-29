package leo20

import leo14.Script

sealed class Body
data class ScriptBody(val script: Script) : Body()
object NumberPlusBody : Body()
object NumberMinusBody : Body()
object NumberEqualsBody : Body()

fun body(script: Script): Body = ScriptBody(script)

fun Scope.unsafeValue(body: Body): Value =
	when (body) {
		is ScriptBody -> value(body.script)
		NumberPlusBody -> unsafeGet("number").unsafeNumberPlus(unsafeGet("plus", "number"))
		NumberMinusBody -> unsafeGet("number").unsafeNumberMinus(unsafeGet("minus", "number"))
		NumberEqualsBody -> unsafeGet("number").equals(unsafeGet("equals", "number")).value
	}