package leo14.untyped

import leo14.Script

sealed class Body
data class GivesBody(val script: Script) : Body()
data class DoesBody(val script: Script) : Body()
data class RecurseBody(val script: Script) : Body()
data class MacroBody(val script: Script) : Body()

fun givesBody(script: Script): Body = GivesBody(script)
fun doesBody(script: Script): Body = DoesBody(script)
fun recurseBody(script: Script): Body = RecurseBody(script)
fun compileBody(script: Script): Body = MacroBody(script)

fun Body.apply(scope: Scope, given: Thunk): Applied =
	when (this) {
		is GivesBody -> applied(function(scope, script).apply(given))
		is DoesBody -> applied(function(scope, script).doWith(given))
		is RecurseBody -> applied(scope
			.resolver(given.value.sequenceOrNull!!.previousThunk)
			.compile(script)
			.thunk)
		is MacroBody -> applied(function(scope, script).apply(given).value.script)
	}
