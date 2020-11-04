package leo21.type

import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.LinkScript
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.UnitScript
import leo14.fieldOrNull
import leo14.isEmpty
import leo14.lineSeq

val Script.type: Type
	get() =
		emptyTypeCompiler.fold(lineSeq.reverse) { plus(it) }.type

val Script.struct: Struct
	get() =
		struct().fold(lineSeq.reverse) { plus(it.typeLine) }

val Script.choice: Choice
	get() =
		choice().fold(lineSeq.reverse) { plus(it.typeLine) }

val ScriptLine.typeLine: Line
	get() =
		when (this) {
			is LiteralScriptLine -> error("literal not a type")
			is FieldScriptLine -> field.typeLine
		}

val ScriptField.typeLine: Line
	get() =
		keywordTypeLineOrNull ?: rawTypeLine

val ScriptField.keywordTypeLineOrNull: Line?
	get() =
		when (string) {
			"number" -> notNullIf(rhs.isEmpty) { doubleLine }
			"text" -> notNullIf(rhs.isEmpty) { stringLine }
			"function" -> line(rhs.arrowLine)
			else -> null
		}

val ScriptField.rawTypeLine: Line
	get() =
		string lineTo rhs.type

val Script.arrowLine: Arrow
	get() =
		when (this) {
			is UnitScript -> error("not a function")
			is LinkScript -> link.line.fieldOrNull.notNullOrError("not a function").let { field ->
				if (field.string != "doing") error("not a function")
				else link.lhs.type arrowTo field.rhs.type
			}
		}