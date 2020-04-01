package leo14.untyped

import leo14.Script

sealed class Body
data class DoesBody(val script: Script) : Body()
data class MacroBody(val script: Script) : Body()

fun doesBody(script: Script): Body = DoesBody(script)
fun expandsBody(script: Script): Body = MacroBody(script)

fun Body.apply(scope: Scope, given: Thunk): Applied =
	when (this) {
		is DoesBody -> applied(action(scope, script).with(given))
		is MacroBody -> applied(action(scope, script).applyScript(given).value.script)
	}
