package leo21.token.typer

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.type.Choice
import leo21.type.Type
import leo21.type.choice
import leo21.type.plus
import leo21.type.type

data class TokenChoiceCompiler(
	val parentOrNull: ChoiceParent?,
	val choice: Choice
)

val emptyTokenChoiceCompiler = TokenChoiceCompiler(null, choice())

fun TokenChoiceCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> plusBegin(token.begin.string)
		is EndToken -> parentOrNull!!.plus(choice)
	}

fun TokenChoiceCompiler.plusBegin(name: String): TokenProcessor =
	TypeCompilerTokenProcessor(
		TokenTypeCompiler(
			ChoiceNameTypeParent(this, name),
			type()))

fun TokenChoiceCompiler.plus(name: String, rhs: Type): TokenChoiceCompiler =
	set(choice.plus(name compiledLineTo rhs))

fun TokenChoiceCompiler.set(choice: Choice): TokenChoiceCompiler =
	copy(choice = choice)