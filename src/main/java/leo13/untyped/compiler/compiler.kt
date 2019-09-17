package leo13.untyped.compiler

import leo.base.fold
import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.script.tokenSeq
import leo13.token.*
import leo13.untyped.expression.*
import leo13.untyped.pattern.*
import leo13.untyped.value.function
import leo13.untyped.value.value
import leo9.Stack
import leo9.fold
import leo9.reverse

data class Compiler(
	val converter: Converter<Compiled, Token>,
	val context: Context,
	val compiled: Compiled
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"expression" lineTo script(
				"compiler" lineTo script(
					converter.scriptingLine,
					context.scriptingLine,
					compiled.scriptingLine))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		when (name) {
			"apply" -> beginApply
			"define" -> beginDefine
			"given" -> beginGiven
			"gives" -> beginGives
			"in" -> beginIn
			"of" -> beginOf
			"content" -> beginContent
			"previous" -> beginPrevious
			"set" -> beginSet
			"switch" -> beginSwitch
			"pattern" -> beginPattern
			else -> beginOther(name)
		}

	val end
		get() =
			converter.convert(compiled)

	val beginApply: Processor<Token>
		get() =
			compiled.pattern.linkOrNull?.item?.arrowOrNull?.let { arrow ->
				compiler(
					converter { parameterCompiled ->
						if (parameterCompiled.pattern != arrow.lhs) tracedError(
							"mismatch" lineTo script(
								"expected" lineTo script(arrow.lhs.scriptingLine),
								"actual" lineTo script(parameterCompiled.pattern.scriptingLine)))
						else set(
							compiled(
								compiled.expression.plus(apply(parameterCompiled.expression).op),
								arrow.rhs))
					},
					context)
			} ?: tracedError("not" lineTo script("arrow"))

	val beginDefine: Processor<Token> get() = TODO()

	val beginContent: Processor<Token>
		get() =
			compiler(
				converter { plusContent(it) },
				context)

	val beginGives: Processor<Token>
		get() =
			compiled.pattern.staticScriptOrNull?.let { script ->
				(patternCompiler(
					converter { pattern ->
						compiler(
							converter { compiled ->
								set(
									compiled(
										expression(value(leo13.untyped.value.item(function(given(value()), compiled.expression))).op),
										pattern(item(pattern arrowTo compiled.pattern))))
							},
							context.bind(pattern))
					},
					context.arrows) as Processor<Token>).fold(script.tokenSeq) { process(it) }
					.process(token(closing))
			} ?: tracedError("not" lineTo script("static"))

	val beginGiven: Processor<Token>
		get() =
			compiler(
				converter { plusGiven(it) },
				context)

	val beginIn: Processor<Token>
		get() =
			compiler(
				converter { plusIn(it) },
				context.bind(compiled.pattern))

	val beginOf: Processor<Token>
		get() =
			patternCompiler(
				converter { plusOf(it) },
				context.arrows)

	val beginPrevious: Processor<Token>
		get() =
			compiler(
				converter { plusPrevious(it) },
				context)

	val beginSet: Processor<Token>
		get() =
			lineCompiler(
				converter { plusSet(it) },
				context)

	val beginSwitch: Processor<Token>
		get() =
			compiled
				.pattern
				.linkOrNull
				?.let { patternLink0 ->
					patternLink0
						.item
						.lineOrNull?.let { line ->
							line
								.rhs
								.linkOrNull
								?.let { patternLink ->
									patternLink
										.item
										.choiceOrNull
										?.let { choice ->
											switchCompiler(
												converter { plus(it) },
												context,
												choice.eitherStack.reverse,
												compiled(
													switch(),
													compiled.pattern))
										}
								}
						}
						?: tracedError("expected" lineTo script("choice"))
				} ?: tracedError("empty" lineTo script())

	val beginPattern: Processor<Token>
		get() =
			compiler(
				converter { plusPattern(it) },
				context)

	fun beginOther(name: String): Processor<Token> =
		compiler(
			converter { plus(name lineTo it) },
			context)
}

fun compiler() =
	compiler(errorConverter(), context(), compiled())

fun Converter<Compiled, Token>.compiler() =
	compiler(this, context(), compiled())

fun compiler(
	converter: Converter<Compiled, Token>,
	context: Context,
	compiled: Compiled = compiled()) =
	Compiler(converter, context, compiled)

fun Compiler.set(context: Context) =
	copy(context = context)

fun Compiler.set(compiled: Compiled) =
	copy(compiled = compiled)

fun Compiler.plus(line: CompiledLine): Compiler =
	if (line.rhs.pattern.isEmpty && !compiled.pattern.isEmpty) tracedError("normalization" lineTo script())
	else when (line.name) {
		else -> plusOther(line)
	}

fun Compiler.plusContent(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty) tracedError()
	else rhs.contentOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusIn(rhs: Compiled): Compiler =
	set(compiled.plusIn(rhs))

fun Compiler.plusGiven(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty || !rhs.pattern.isEmpty) tracedError()
	else set(compiled(expression(given.op), context.givenPattern))

fun Compiler.plusPrevious(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty) tracedError()
	else rhs.previousOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusOf(rhs: Pattern): Compiler =
	if (rhs.contains(compiled.pattern)) set(compiled(compiled.expression, rhs))
	else tracedError("not" lineTo script(
		rhs.scriptingLine,
		"contains" lineTo script(compiled.pattern.scriptingLine)))

fun Compiler.plus(switchCompiled: SwitchCompiled): Compiler =
	set(compiled.plus(switchCompiled))

fun Compiler.plusSet(lineStack: Stack<CompiledLine>): Compiler =
	fold(lineStack.reverse) { plusSet(it) }

fun Compiler.plusSet(line: CompiledLine): Compiler =
	compiled.pattern.setOrNull(line.patternLine)
		?.let { setPattern ->
			set(
				compiled(
					compiled.expression.plus(set(line.expressionLine).op),
					setPattern))
		}?: tracedError("set" lineTo script())

fun Compiler.plusOther(line: CompiledLine): Compiler =
	plusGetOrNull(line) ?: append(line)

fun Compiler.plusGetOrNull(line: CompiledLine): Compiler? =
	line.rhs.getOrNull(line.name)?.run { set(this) } // TODO: Don't set(), but plus(line)

fun Compiler.plusPattern(rhs: Compiled): Compiler =
	append("pattern" lineTo compiled(rhs.pattern.scriptingLine.rhs))

fun Compiler.append(line: CompiledLine): Compiler =
	set(compiled.plus(line))
