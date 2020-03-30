package leo14.untyped

import leo14.Script

sealed class Body
data class ThunkBody(val thunk: Thunk) : Body()
data class EvalBody(val script: Script) : Body()
data class RecurseBody(val script: Script) : Body()
data class MacroBody(val script: Script) : Body()

fun body(thunk: Thunk): Body = ThunkBody(thunk)
fun evalBody(script: Script): Body = EvalBody(script)
fun recurseBody(script: Script): Body = RecurseBody(script)
fun compileBody(script: Script): Body = MacroBody(script)

fun Body.apply(scope: Scope, given: Thunk): Applied =
	when (this) {
		is ThunkBody -> applied(thunk)
		is EvalBody -> applied(function(scope, script).apply(given))
		is RecurseBody -> applied(scope
			.resolver(given.value.sequenceOrNull!!.previousThunk)
			.compile(script)
			.thunk)
		is MacroBody -> applied(function(scope, script).apply(given).value.script)
	}
