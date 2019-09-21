package leo13.compiler

import leo.base.ifOrNull
import leo13.*
import leo13.expression.*
import leo13.pattern.Pattern
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.pattern.plusReversed
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.value.item
import leo13.value.value

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
			"as" -> beginAs
			"define" -> beginDefine
			"given" -> beginGiven
			"function" -> beginFunction
			"in" -> beginIn
			"of" -> beginOf
			"content" -> beginContent
			"previous" -> beginPrevious
			"set" -> beginSet
			"match" -> beginMatch
			"matching" -> beginMatching
			"pattern" -> beginPattern
			"compiler" -> beginCompiler
			else -> beginOther(name)
		}

	val end
		get() =
			converter.convert(compiled)

	val beginApply: Processor<Token>
		get() =
			compiled
				.pattern
				.arrowOrNull
				?.let { arrow ->
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
				}
				?: tracedError("expected" lineTo script("arrow"))

	val beginAs: Processor<Token>
		get() =
			compiler(
				converter { plusAs(it) },
				context)

	val beginDefine: Processor<Token>
		get() =
			DefineCompiler(
				converter { newContext ->
					copy(context = newContext)
				},
				context,
				pattern())

	val beginContent: Processor<Token>
		get() =
			compiler(
				converter { plusContent(it) },
				context)

	val beginFunction: Processor<Token>
		get() =
			if (!compiled.isEmpty)
				tracedError<Processor<Token>>("not" lineTo script("expected" lineTo script("function")))
			else FunctionCompiler(
				converter { compiledFunction ->
					set(
						compiled(
							expression(op(value(item(compiledFunction.function)))),
							pattern(compiledFunction.arrow)))
				},
				context,
				pattern(),
				null)

	val beginGiven: Processor<Token>
		get() =
			compiler(
				converter { plusGiven(it) },
				context)

	val beginMatching: Processor<Token>
		get() =
			compiler(
				converter { plusSwitched(it) },
				context)

	val beginIn: Processor<Token>
		get() =
			compiler(
				converter { plusIn(it) },
				context.give(compiled.pattern))

	val beginOf: Processor<Token>
		get() =
			patternCompiler(
				converter { plusOf(it) },
				false,
				context.patternDefinitions)

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

	val beginMatch: Processor<Token>
		get() =
			compiled
				.pattern
				.linkOrNull
				?.let { link ->
					link
						.line
						.let { line ->
							line
								.rhs
								.optionsOrNull
								?.let { options ->
									switchCompiler(
										converter { plus(it) },
										context,
										leo13.pattern.options().plusReversed(options),
										compiled(
											switch(),
											compiled.pattern))
								}
						}
						?: tracedError("expected" lineTo script("options"))
				} ?: tracedError("empty" lineTo script())

	val beginPattern: Processor<Token>
		get() =
			compiler(
				converter { plusPattern(it) },
				context)

	val beginCompiler: Processor<Token>
		get() =
			compiler(
				converter { plusCompiler(it) },
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
	copy(compiled = context.functions.resolve(context.patterns.resolve(compiled)))

fun Compiler.plus(line: CompiledLine): Compiler =
	plusOther(line)

// TODO: Support multiple names.
fun Compiler.plusAs(rhs: Compiled): Compiler =
	rhs
		.pattern
		.onlyNameOrNull
		?.let { name ->
			set(
				compiled(
					compiled.expression.plus(wrap(name).op),
					pattern(name lineTo compiled.pattern)))
		}
		?: tracedError("expected" lineTo script("name"))

fun Compiler.plusContent(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty) tracedError()
	else rhs.contentOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusIn(rhs: Compiled): Compiler =
	set(compiled.plusIn(rhs))

fun Compiler.plusGiven(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty || !rhs.pattern.isEmpty) tracedError()
	else set(compiled(expression(given.op), context.givenPattern))

fun Compiler.plusSwitched(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty || !rhs.pattern.isEmpty) tracedError()
	else set(compiled(expression(switched.op), context.switchedPattern))

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
	ifOrNull(line.rhs.pattern.isEmpty) {
		compiled.getOrNull(line.name)?.run { set(this) }
	}

fun Compiler.plusPattern(rhs: Compiled): Compiler =
	append("pattern" lineTo compiled(rhs.pattern.scriptingLine.rhs))

fun Compiler.plusCompiler(rhs: Compiled): Compiler =
	if (!rhs.pattern.isEmpty) tracedError("expected" lineTo script("empty"))
	else set(compiled(script(scriptingLine)))

fun Compiler.append(line: CompiledLine): Compiler =
	set(compiled.plus(line))
