package leo21.token.type.compiler

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

data class ChoiceCompiler(
	val parentOrNull: ChoiceParent?,
	val choice: Choice
)

val emptyChoiceCompiler = ChoiceCompiler(null, choice())

fun ChoiceCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> plusBegin(token.begin.string)
		is EndToken -> parentOrNull!!.plus(choice)
	}

fun ChoiceCompiler.plusBegin(name: String): TokenProcessor =
	TypeCompilerTokenProcessor(
		TypeCompiler(
			ChoiceNameTypeParent(this, name),
			type()))

fun ChoiceCompiler.plus(name: String, rhs: Type): ChoiceCompiler =
	set(choice.plus(name compiledLineTo rhs))

fun ChoiceCompiler.set(choice: Choice): ChoiceCompiler =
	copy(choice = choice)