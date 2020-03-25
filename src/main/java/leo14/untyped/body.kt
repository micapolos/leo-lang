package leo14.untyped

import leo14.Script

sealed class Body
data class ThunkBody(val thunk: Thunk) : Body()
data class EvalBody(val script: Script) : Body()
data class RecurseBody(val script: Script) : Body()

fun body(thunk: Thunk): Body = ThunkBody(thunk)
fun evalBody(script: Script): Body = EvalBody(script)
fun recurseBody(script: Script): Body = RecurseBody(script)

fun Body.apply(context: Context, given: Thunk): Thunk =
	when (this) {
		is ThunkBody -> thunk
		is EvalBody -> function(context, script).apply(given)
		is RecurseBody -> context
			.resolver(given.value.sequenceOrNull!!.tail)
			.compile(script)
			.thunk
	}
