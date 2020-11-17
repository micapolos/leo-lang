package leo21.token.type.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.type.Choice
import leo21.type.Type
import leo21.type.choice
import leo21.type.plus
import leo21.type.type

data class ChoiceCompiler(
	val parentOrNull: ChoiceParent?,
	val lines: Lines,
	val choice: Choice
)

val emptyChoiceCompiler = ChoiceCompiler(null, emptyLines, choice())

fun ChoiceCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> plusBegin(token.begin.string)
		is EndToken -> parentOrNull!!.plus(choice)
	}

fun ChoiceCompiler.plusBegin(name: String): Processor =
	TypeCompilerProcessor(
		TypeCompiler(
			ChoiceNameTypeParent(this, name),
			lines,
			type()))

fun ChoiceCompiler.plus(name: String, rhs: Type): ChoiceCompiler =
	set(choice.plus(lines.resolve(name compiledLineTo rhs)))

fun ChoiceCompiler.set(choice: Choice): ChoiceCompiler =
	copy(choice = choice)