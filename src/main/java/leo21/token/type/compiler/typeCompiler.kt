package leo21.token.type.compiler

import leo.base.notNullIf
import leo13.firstEither
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.error
import leo15.dsl.*
import leo21.token.processor.ArrowCompilerProcessor
import leo21.token.processor.ChoiceCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.type.Line
import leo21.type.Type
import leo21.type.choice
import leo21.type.numberLine
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

fun TypeCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> error { not { expected { literal } } }
		is BeginToken -> plusBegin(token.begin.string)
		is EndToken -> parentOrNull!!.plus(type)
	}

fun TypeCompiler.plusBegin(name: String): Processor =
	when (name) {
		"choice" ->
			if (type.isEmpty)
				ChoiceCompilerProcessor(
					ChoiceCompiler(
						TypeCompilerChoiceParent(this),
						lines,
						choice()))
			else error { not { expected { word { choice } } } }
		"function" -> ArrowCompilerProcessor(
			ArrowCompiler(
				TypeCompilerArrowParent(this),
				lines,
				type().firstEither()))
		"recursive" ->
			if (!type.isEmpty) TypeCompilerProcessor(
				TypeCompiler(
					RecursiveTypeParent(this),
					lines,
					type()))
			else error { not { expected { word { recursive } } } }
		else -> TypeCompilerProcessor(
			TypeCompiler(
				TypeNameTypeParent(this, name),
				lines,
				type()))
	}

fun TypeCompiler.plus(name: String, rhs: Type): Processor =
	plus(lines.resolve(name compiledLineTo rhs))

fun TypeCompiler.plus(line: Line): Processor =
	process(type.plus(line))

fun TypeCompiler.set(type: Type): TypeCompiler =
	copy(type = type)

fun TypeCompiler.process(type: Type): Processor =
	TypeCompilerProcessor(set(type))

infix fun String.compiledLineTo(rhs: Type): Line =
	when (this) {
		"number" -> notNullIf(rhs == type()) { numberLine }
		"text" -> notNullIf(rhs == type()) { stringLine }
		else -> null
	} ?: this lineTo rhs
