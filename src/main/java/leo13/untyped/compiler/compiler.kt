package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.*
import leo13.untyped.pattern.*
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
			"define" -> beginOf
			"given" -> beginGiven
			"in" -> beginIn
			"function" -> beginFunction
			"of" -> beginOf
			"content" -> beginContent
			"previous" -> beginPrevious
			"set" -> beginSet
			"switch" -> beginSwitch
			else -> beginOther(name)
		}

	val end
		get() =
			converter.convert(compiled)

	val beginDefine: Processor<Token> get() = TODO()

	val beginContent: Processor<Token>
		get() =
			compiler(
				converter { plusContent(it) },
				context)

	val beginFunction: Processor<Token>
		get() = FunctionCompiler(
			converter { functionCompiled ->
				set(
					compiled(
						compiled.expression.plus(leo13.untyped.value.item(functionCompiled.function).op),
						pattern(item(functionCompiled.arrow))))
			},
			context,
			pattern(),
			null)

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
				pattern())

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

fun Compiler.append(line: CompiledLine): Compiler =
	set(compiled.plus(line))
