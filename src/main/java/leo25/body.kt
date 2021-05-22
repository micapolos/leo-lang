package leo25

import leo.base.orNullApply
import leo14.Script
import leo14.literal

sealed class Body
data class ScriptBody(val script: Script) : Body()
data class FnBody(val fn: Context.(Value) -> Value) : Body()

fun body(script: Script): Body = ScriptBody(script)
fun body(fn: Context.(Value) -> Value): Body = FnBody(fn)

tailrec fun Body.apply(context: Context, given: Value): Value =
	when (this) {
		is FnBody -> context.fn(given)
		is ScriptBody -> {
			val result = context.plusGiven(given).interpretedValue(script)
			val repeatValue = result.repeatValueOrNull
			if (repeatValue != null) apply(context, repeatValue)
			else result
		}
	}

val textPlusTextBody
	get() = body { value ->
		value
			.resolveInfixOrNull { rhs ->
				textOrNull?.orNullApply(rhs.textOrNull) {
					value(line(literal(plus(it))))
				}
			}
			?: value
	}
