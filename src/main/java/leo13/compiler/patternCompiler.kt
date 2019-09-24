package leo13.compiler

import leo13.*
import leo13.pattern.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class PatternCompiler(
	val converter: Converter<Pattern, Token>,
	val partial: Boolean,
	val definitions: PatternDefinitions,
	val recurseDefinitionOrNull: RecurseDefinition?,
	val pattern: Pattern
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				partialName lineTo script("$partial"),
				definitions.scriptingLine,
				recurseDefinitionOrNull?.scriptingLine ?: definitionName lineTo script(recurseName lineTo script(noneName)),
				pattern.scriptingLine)

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
			if (!pattern.isEmpty) tracedError(notName lineTo script(expectedName lineTo script(optionsName)))
			else OptionsCompiler(
				converter { options ->
					patternCompiler(
						converter,
						partial,
						definitions,
						recurseDefinitionOrNull?.recurseIncrease,
						pattern(node(options)))
				},
				definitions,
				recurseDefinitionOrNull,
				options())

	fun beginOther(name: String): Processor<Token> =
		patternCompiler(
			converter { plus(name lineTo it) },
			false,
			definitions,
			recurseDefinitionOrNull?.recurseIncrease,
			pattern())

	val end: Processor<Token> get() = converter.convert(pattern)
}

fun patternCompiler(
	converter: Converter<Pattern, Token> = errorConverter(),
	parent: Boolean = false,
	definitions: PatternDefinitions = patternDefinitions(),
	recurseDefinitionOrNull: RecurseDefinition? = null,
	pattern: Pattern = pattern()) =
	PatternCompiler(converter, parent, definitions, recurseDefinitionOrNull, pattern)

fun PatternCompiler.plus(line: PatternLine) =
	set(recurseDefinitionOrNull.orNullResolve(pattern.plus(definitions.resolve(line))))

fun PatternCompiler.set(newPattern: Pattern) =
	if (partial) converter.convert(newPattern)
	else PatternCompiler(converter, partial, definitions, recurseDefinitionOrNull, newPattern)
