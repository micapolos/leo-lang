package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.expression
import leo13.untyped.expression.given
import leo13.untyped.expression.op
import leo13.untyped.expression.switch
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
			"of" -> beginOf
			"everything" -> beginEverything
			"previous" -> beginPrevious
			"set" -> beginSet
			"switch" -> beginSwitch
			else -> beginOther(name)
		}

	val end
		get() =
			converter.convert(compiled)

	val beginDefine: Processor<Token> get() = TODO()

	val beginEverything: Processor<Token>
		get() =
			compiler(
				converter { plusEverything(it) },
				context)

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
						?: tracedError("expected" lineTo script("choice"))
				} ?: tracedError("empty" lineTo script())


	fun beginOther(name: String): Processor<Token> =
		compiler(
			converter { plus(name lineTo it) },
			context)
}

fun compiler() =
	compiler(errorConverter(), context(), compiled())

fun compiler(
	converter: Converter<Compiled, Token>,
	context: Context,
	compiled: Compiled = compiled()) =
	Compiler(converter, context, compiled)

fun Compiler.set(context: Context) =
	copy(context = context)

fun Compiler.set(compiled: Compiled) =
	copy(compiled = compiled)

// TODO: Implement normalization outside of the compiler, as Processor<Token>
fun Compiler.plus(line: CompiledLine): Compiler =
	if (line.rhs.pattern.isEmpty) plus(line.name)
	else plusNormalized(line)

fun Compiler.plusEverything(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty) tracedError()
	else rhs.everythingOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusIn(rhs: Compiled): Compiler =
	set(compiled.plusIn(rhs))

fun Compiler.plusGiven(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty || !rhs.pattern.isEmpty) tracedError()
	else set(compiled(expression(given.op), context.givenPattern))

fun Compiler.plusPrevious(rhs: Compiled): Compiler =
	if (!compiled.pattern.isEmpty) tracedError()
	else rhs.previousOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusOf(rhs: Pattern): Compiler =
	if (!rhs.contains(compiled.pattern)) tracedError()
	else set(compiled(compiled.expression, rhs))

fun Compiler.plus(switchCompiled: SwitchCompiled): Compiler =
	set(compiled.plus(switchCompiled))

fun Compiler.plus(name: String): Compiler =
	set(compiled()).plusNormalized(name lineTo compiled)

fun Compiler.plusNormalized(line: CompiledLine): Compiler =
	when (line.name) {
		else -> plusOther(line)
	}

fun Compiler.plusSet(lineStack: Stack<CompiledLine>): Compiler =
	fold(lineStack.reverse) { plus(it) }

fun Compiler.plusSet(line: CompiledLine): Compiler =
	TODO()

fun Compiler.plusOther(line: CompiledLine): Compiler =
	plusGetOrNull(line) ?: append(line)

fun Compiler.plusGetOrNull(line: CompiledLine): Compiler? =
	line.rhs.getOrNull(line.name)?.run { set(this) } // TODO: Don't set(), but plus(line)

fun Compiler.append(line: CompiledLine): Compiler =
	set(compiled.plus(line))
