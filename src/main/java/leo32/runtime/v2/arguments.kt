package leo32.runtime.v2

import leo.base.Empty

sealed class Arguments
data class EmptyArguments(val empty: Empty) : Arguments()
data class ScriptArguments(val script: Script) : Arguments()
data class EntryArguments(val entry: Entry) : Arguments()

val Empty.arguments
	get() =
		EmptyArguments(this) as Arguments

fun arguments(script: Script) =
	ScriptArguments(script) as Arguments

fun arguments(entry: Entry) =
	EntryArguments(entry) as Arguments

val Arguments.lhsOrNull
	get() =
		when (this) {
			is EmptyArguments -> null
			is ScriptArguments -> null
			is EntryArguments -> entry.lhs
		}

val Arguments.rhsOrNull
	get() =
		when (this) {
			is EmptyArguments -> null
			is ScriptArguments -> script
			is EntryArguments -> entry.rhs
		}
