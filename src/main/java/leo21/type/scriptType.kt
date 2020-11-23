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
import leo14.literalOrNull
import leo14.numberOrNull
import leo14.onlyLineOrNull

val Script.type: Type
	get() =
		type().fold(lineSeq.reverse) { plus(it.typeLine) }

val ScriptLine.typeLine: Line
	get() =
		when (this) {
			is LiteralScriptLine -> error("literal not a type")
			is FieldScriptLine -> field.typeLine
		}

val ScriptField.typeLine: Line
	get() =
		when (string) {
			"choice" -> line(choice().fold(rhs.lineSeq.reverse) { plus(it.typeLine) })
			"number" -> notNullIf(rhs.isEmpty) { numberLine }
			"text" -> notNullIf(rhs.isEmpty) { stringLine }
			"function" -> line(rhs.arrowLine)
			"recursive" -> line(recursive(rhs.onlyLineOrNull!!.fieldOrNull!!.typeLine))
			"recurse" -> line(recurse(rhs.onlyLineOrNull!!.literalOrNull!!.numberOrNull!!.bigDecimal.intValueExact()))
			"word" -> rhs.onlyLineOrNull!!.fieldOrNull!!.rawTypeLine
			else -> null
		} ?: rawTypeLine

val ScriptField.rawTypeLine: Line
	get() =
		string lineTo rhs.type

val Script.arrowLine: Arrow
	get() =
		when (this) {
			is UnitScript -> error("not a function")
			is LinkScript -> link.line.fieldOrNull.notNullOrError("not a function").let { field ->
				if (field.string != "does") error("not a function")
				else link.lhs.type arrowTo field.rhs.type
			}
		}