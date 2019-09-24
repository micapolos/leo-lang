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
	val traceOrNull: PatternTrace?,
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
					PatternCompiler(
						converter,
						partial,
						definitions,
						recurseDefinitionOrNull?.recurseIncrease,
						traceOrNull,
						pattern(options))
				},
				definitions,
				recurseDefinitionOrNull,
				traceOrNull,
				options())

	fun beginOther(name: String): Processor<Token> =
		PatternCompiler(
			converter { plus(name lineTo it) },
			false,
			definitions,
			recurseDefinitionOrNull?.recurseIncrease,
			traceOrNull.orNullPlus(name lineTo pattern()),
			pattern())

	val end: Processor<Token> get() = converter.convert(pattern)

	fun plus(line: PatternLine) =
		set(recurseDefinitionOrNull.orNullResolve(pattern.plus(definitions.resolve(line))))

	fun set(newPattern: Pattern) =
		if (partial) converter.convert(newPattern)
		else PatternCompiler(converter, partial, definitions, recurseDefinitionOrNull, traceOrNull, newPattern)
}

fun patternCompiler() = PatternCompiler(errorConverter(), false, patternDefinitions(), null, null, pattern())