package leo14.untyped.typed.lambda

import leo14.*
import leo14.untyped.typed.*

val Type.staticScriptOrNull: Script?
	get() =
		when (this) {
			EmptyType -> script()
			AnythingType -> null
			NothingType -> null
			is LinkType -> link.staticScriptOrNull
			is AlternativeType -> null
			is FunctionType -> null
			is RepeatingType -> null
			is RecursiveType -> null
			RecurseType -> null
		}

val TypeLink.staticScriptOrNull: Script?
	get() =
		lhs.staticScriptOrNull?.let { lhs ->
			line.staticScriptLineOrNull?.let { line ->
				lhs.plus(line)
			}
		}

val TypeLine.staticScriptLineOrNull: ScriptLine?
	get() =
		when (this) {
			is LiteralTypeLine -> line(literal)
			is FieldTypeLine -> field.staticScriptFieldOrNull?.let { line(it) }
			JavaTypeLine -> null
		}

val TypeField.staticScriptFieldOrNull: ScriptField?
	get() =
		rhs.staticScriptOrNull?.let { rhs ->
			name fieldTo rhs
		}