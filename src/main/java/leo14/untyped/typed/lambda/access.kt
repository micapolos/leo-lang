package leo14.untyped.typed.lambda

import leo.base.notNullIf
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.untyped.nativeName
import leo14.untyped.numberName
import leo14.untyped.textName
import leo14.untyped.typed.FieldTypeLine
import leo14.untyped.typed.LiteralTypeLine
import leo14.untyped.typed.NativeTypeLine

fun Compiled.get(name: String): Compiled? =
	linkOrNull?.onlyLineOrNull?.fieldOrNull?.rhs?.lineOrNull(name)?.let { compiled(it) }

fun Compiled.lineOrNull(name: String): CompiledLine? =
	linkOrNull?.lineOrNull(name)

fun CompiledLink.lineOrNull(name: String): CompiledLine? =
	lhs.lineOrNull(name) ?: line.lineOrNull(name)

// TODO: handle meta!!!
fun CompiledLine.lineOrNull(name: String): CompiledLine? =
	when (typeLine) {
		is LiteralTypeLine ->
			when (typeLine.literal) {
				is StringLiteral -> notNullIf(name == textName)
				is NumberLiteral -> notNullIf(name == numberName)
			}
		is FieldTypeLine ->
			notNullIf(typeLine.field.name == name)
		NativeTypeLine ->
			notNullIf(name == nativeName)
	}
