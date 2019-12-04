package leo14.typed

import leo14.*
import leo14.typed.compiler.LiteralCompile

fun <T> Script.staticTyped(literalCompile: LiteralCompile<T>): Typed<T> =
		when (this) {
			is UnitScript -> typed()
			is LinkScript -> link.lhs.staticTyped(literalCompile).plus(link.line.staticTypedLine(literalCompile))
		}

fun <T> ScriptLine.staticTypedLine(literalCompile: LiteralCompile<T>): TypedLine<T> =
		when (this) {
			is LiteralScriptLine -> literal.literalCompile()
			is FieldScriptLine -> line(field.staticTypedField(literalCompile))
		}

fun <T> ScriptField.staticTypedField(literalCompile: LiteralCompile<T>): TypedField<T> =
	string fieldTo rhs.staticTyped(literalCompile)
