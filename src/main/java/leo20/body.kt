package leo20

import leo14.Script
import leo15.dsl.*

sealed class Body
data class ScriptBody(val script: Script) : Body()
object NumberPlusBody : Body()
object NumberMinusBody : Body()
object NumberEqualsBody : Body()
object StringAppendBody : Body()

fun body(script: Script): Body = ScriptBody(script)

fun Dictionary.unsafeValue(body: Body): Value =
	when (body) {
		is ScriptBody -> value(body.script)
		NumberPlusBody -> unsafeGiven.unsafeGet("number").unsafeNumberPlus(unsafeGiven.unsafeGet("plus").unsafeGet("number"))
		NumberMinusBody -> unsafeGiven.unsafeGet("number").unsafeNumberMinus(unsafeGiven.unsafeGet("minus").unsafeGet("number"))
		NumberEqualsBody -> unsafeGiven.unsafeGet("number").equals(unsafeGiven.unsafeGet("equals").unsafeGet("number")).value
		StringAppendBody -> unsafeGiven.unsafeGet("text").unsafeTextAppend(unsafeGiven.unsafeGet("append").unsafeGet("text"))
	}