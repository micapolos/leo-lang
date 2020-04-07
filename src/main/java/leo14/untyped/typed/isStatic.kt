package leo14.untyped.typed

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.untyped.*

val Type.isStatic: Boolean
	get() =
		this is StaticType

val TypeLine.isStatic: Boolean
	get() =
		(this is FieldTypeLine) && field.isStatic

val TypeField.isStatic: Boolean
	get() =
		rhs.isStatic

// === script based ===

val Script.typeStaticScriptOrNull: Script?
	get() =
		linkOrNull?.typeStaticScriptOrNull

val ScriptLink.typeStaticScriptOrNull: Script?
	get() =
		ifOrNull(lhs.isEmpty) { line.fieldOrNull?.typeStaticScriptOrNull }

val ScriptField.typeStaticScriptOrNull: Script?
	get() =
		notNullIf(string == staticName) { rhs }

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

