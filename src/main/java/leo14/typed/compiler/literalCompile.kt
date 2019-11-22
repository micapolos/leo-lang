package leo14.typed.compiler

import leo.base.fold
import leo14.*
import leo14.typed.*

typealias LiteralCompile<T> = Literal.() -> TypedLine<T>

fun <T> typed(script: Script, literalCompile: LiteralCompile<T>): Typed<T> =
	typed<T>().plus(script, literalCompile)

fun <T> Typed<T>.plus(script: Script, literalCompile: LiteralCompile<T>): Typed<T> =
	fold(script.lineSeq.reversed()) { plus(it, literalCompile) }

fun <T> Typed<T>.plus(scriptLine: ScriptLine, literalCompile: LiteralCompile<T>): Typed<T> =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal.literalCompile())
		is FieldScriptLine -> plus(scriptLine.field.string lineTo typed(scriptLine.field.rhs, literalCompile))
	}
