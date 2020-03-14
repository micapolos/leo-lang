package leo14.untyped

import leo14.*

val ScriptLink.resolve: Script?
	get() =
		when (line) {
			is LiteralScriptLine -> null
			is FieldScriptLine ->
				when (line.field.rhs) {
					is UnitScript -> lhs.resolve(line.field.string)
					is LinkScript -> null
				}
		}

fun Script.resolve(name: String) =
	resolveAccess(name)

fun Script.resolveAccess(name: String) =
	access(name)?.let { script(it) }
