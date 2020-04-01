package leo14.untyped

import leo14.Script

sealed class Body
data class GivesBody(val script: Script) : Body()
data class DoesBody(val script: Script) : Body()
data class MacroBody(val script: Script) : Body()

fun givesBody(script: Script): Body = GivesBody(script)
fun doesBody(script: Script): Body = DoesBody(script)
fun expandsBody(script: Script): Body = MacroBody(script)

fun Body.apply(scope: Scope, given: Thunk): Applied =
	when (this) {
		is GivesBody -> applied(doing(scope, script).applyGiven(given))
		is DoesBody -> applied(doing(scope, script).with(given))
		is MacroBody -> applied(doing(scope, script).applyGiven(given).value.script)
	}
