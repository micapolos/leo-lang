package leo21.token.type.compiler

import leo.base.failIfOr
import leo.base.notNullIf
import leo13.firstEither
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.ArrowCompilerTokenProcessor
import leo21.token.processor.ChoiceCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.type.Line
import leo21.type.Type
import leo21.type.choice
import leo21.type.doubleLine
import leo21.type.isEmpty
import leo21.type.lineTo
import leo21.type.plus
import leo21.type.stringLine
import leo21.type.type

data class TypeCompiler(
	val parentOrNull: TypeParent?,
	val lines: Lines,
	val type: Type
)

val emptyTypeCompiler = TypeCompiler(null, emptyLines, type())

fun TypeCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> plusBegin(token.begin.string)
		is EndToken -> parentOrNull!!.plus(type)
	}

fun TypeCompiler.plusBegin(name: String): TokenProcessor =
	when (name) {
		"choice" -> failIfOr(!type.isEmpty) {
			ChoiceCompilerTokenProcessor(
				ChoiceCompiler(
					TypeCompilerChoiceParent(this),
					lines,
					choice()))
		}
		"function" -> ArrowCompilerTokenProcessor(
			ArrowCompiler(
				TypeCompilerArrowParent(this),
				lines,
				type().firstEither()))
		"recursive" -> failIfOr(!type.isEmpty) {
			TypeCompilerTokenProcessor(
				TypeCompiler(
					RecursiveTypeParent(this),
					lines,
					type()))
		}
		else -> null
	} ?: TypeCompilerTokenProcessor(
		TypeCompiler(
			TypeNameTypeParent(this, name),
			lines,
			type()))

fun TypeCompiler.plus(name: String, rhs: Type): TokenProcessor =
	plus(lines.resolve(name compiledLineTo rhs))

fun TypeCompiler.plus(line: Line): TokenProcessor =
	process(type.plus(line))

fun TypeCompiler.set(type: Type): TypeCompiler =
	copy(type = type)

fun TypeCompiler.process(type: Type): TokenProcessor =
	TypeCompilerTokenProcessor(set(type))

infix fun String.compiledLineTo(rhs: Type): Line =
	when (this) {
		"number" -> notNullIf(rhs == type()) { doubleLine }
		"text" -> notNullIf(rhs == type()) { stringLine }
		else -> null
	} ?: this lineTo rhs
