package leo14.untyped

import leo14.ScriptLink

sealed class Primitive

object MinusNumberPrimitive : Primitive()
object NumberPlusNumberPrimitive : Primitive()
object NumberMinusNumberPrimitive : Primitive()
object NumberTimesNumberPrimitive : Primitive()
object TextPlusTextPrimitive : Primitive()

fun Primitive.apply(scriptLink: ScriptLink) =
	when (this) {
		MinusNumberPrimitive -> scriptLink.resolveMinusNumber
		NumberPlusNumberPrimitive -> scriptLink.resolveNumberPlusNumber
		NumberMinusNumberPrimitive -> scriptLink.resolveNumberMinusNumber
		NumberTimesNumberPrimitive -> scriptLink.resolveNumberTimesNumber
		TextPlusTextPrimitive -> scriptLink.resolveTextPlusText
	}!!