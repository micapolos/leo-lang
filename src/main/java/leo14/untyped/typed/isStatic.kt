package leo14.untyped.typed

import leo14.*
import leo14.untyped.*

val Type.isStatic: Boolean
	get() =
		when (this) {
			is EmptyType -> true
			is LinkType -> link.isStatic
		}

val TypeLink.isStatic: Boolean
	get() =
		choice.isStatic && lhs.isStatic

val Choice.isStatic: Boolean
	get() =
		when (this) {
			is EmptyChoice -> true
			is LinkChoice -> link.isStatic
		}

val ChoiceLink.isStatic: Boolean
	get() =
		line.isStatic && lhs is EmptyChoice

val TypeLine.isStatic: Boolean
	get() =
		when (this) {
			is LiteralTypeLine -> true
			is FieldTypeLine -> field.isStatic
			NumberTypeLine -> false
			TextTypeLine -> false
			NativeTypeLine -> false
		}

val TypeField.isStatic: Boolean
	get() =
		rhs.isStatic

// === script based ===

val Script.typeIsStatic: Boolean
	get() =
		when (this) {
			is UnitScript -> true
			is LinkScript -> link.typeIsStatic
		}

val ScriptLink.typeIsStatic: Boolean
	get() =
		line.typeIsStatic && lhs.typeIsStatic

val ScriptLine.typeIsStatic: Boolean
	get() =
		when (this) {
			is LiteralScriptLine -> true
			is FieldScriptLine -> field.typeIsStatic
		}

val ScriptField.typeIsStatic: Boolean
	get() =
		when (string) {
			eitherName -> false
			textName -> !rhs.isEmpty
			numberName -> !rhs.isEmpty
			nativeName -> !rhs.isEmpty
			staticName -> rhs.staticTypeIsStatic
			else -> rhs.typeIsStatic
		}

val Script.staticTypeIsStatic: Boolean
	get() =
		when (this) {
			is UnitScript -> true
			is LinkScript -> link.staticTypeIsStatic
		}

val ScriptLink.staticTypeIsStatic: Boolean
	get() =
		line.staticTypeIsStatic && lhs.staticTypeIsStatic

val ScriptLine.staticTypeIsStatic: Boolean
	get() =
		when (this) {
			is LiteralScriptLine -> true
			is FieldScriptLine -> field.staticTypeIsStatic
		}

val ScriptField.staticTypeIsStatic: Boolean
	get() =
		rhs.typeIsStatic

