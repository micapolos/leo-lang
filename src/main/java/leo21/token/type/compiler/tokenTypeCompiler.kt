package leo21.token.type.compiler

import leo.base.notNullIf
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.ChoiceCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.type.Line
import leo21.type.Type
import leo21.type.choice
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.plus
import leo21.type.stringLine
import leo21.type.type

data class TokenTypeCompiler(
	val parentOrNull: TypeParent?,
	val type: Type
)

val emptyTokenTypeCompiler = TokenTypeCompiler(null, type())

fun TokenTypeCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> plusBegin(token.begin.string)
		is EndToken -> parentOrNull!!.plus(type)
	}

fun TokenTypeCompiler.plusBegin(name: String): TokenProcessor =
	when (name) {
		"choice" -> ChoiceCompilerTokenProcessor(
			TokenChoiceCompiler(
				TypeCompilerChoiceParent(this),
				choice()))
		else -> TypeCompilerTokenProcessor(
			TokenTypeCompiler(
				TypeNameTypeParent(this, name),
				type()))
	}

fun TokenTypeCompiler.plus(name: String, rhs: Type): TokenTypeCompiler =
	when (name) {
		"function" -> TODO()
		else -> null
	} ?: set(type.plus(name compiledLineTo rhs))

fun TokenTypeCompiler.set(type: Type): TokenTypeCompiler =
	copy(type = type)

infix fun String.compiledLineTo(rhs: Type): Line =
	when (this) {
		"number" -> notNullIf(rhs == type()) { doubleLine }
		"text" -> notNullIf(rhs == type()) { stringLine }
		else -> null
	} ?: this lineTo rhs
