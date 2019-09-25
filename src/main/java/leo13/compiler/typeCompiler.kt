package leo13.compiler

import leo13.*
import leo13.type.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class TypeCompiler(
	val converter: Converter<Type, Token>,
	val partial: Boolean,
	val context: TypeContext,
	val type: Type
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				partialName lineTo script("$partial"),
				context.scriptingLine,
				type.scriptingLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		when (name) {
			optionsName -> beginOptions
			else -> beginOther(name)
		}

	val beginOptions: Processor<Token>
		get() =
			if (!type.isEmpty) tracedError(notName lineTo script(expectedName lineTo script(optionsName)))
			else OptionsCompiler(
				converter { options ->
					TypeCompiler(
						converter,
						partial,
						context,
						type(options))
				},
				context,
				options())

	fun beginOther(name: String): Processor<Token> =
		TypeCompiler(
			converter { plus(name lineTo it) },
			false,
			context.trace(name),
			type())

	val end: Processor<Token> get() = converter.convert(type)

	fun plus(line: TypeLine) =
		set(type.plus(context.trace.resolveItem(context.definitions.resolve(line))))

	fun set(newType: Type) =
		if (partial) converter.convert(newType)
		else TypeCompiler(converter, partial, context, newType)
}

fun typeCompiler() = TypeCompiler(errorConverter(), false, typeContext(), type())