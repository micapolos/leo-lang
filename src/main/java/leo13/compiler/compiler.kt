package leo13.compiler

import leo.base.ifOrNull
import leo13.*
import leo13.expression.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.type.*
import leo13.value.item
import leo13.value.value

data class Compiler(
	val converter: Converter<ExpressionTyped, Token>,
	val lineConverterOrNull: Converter<Compiled, Token>?,
	val compiled: Compiled) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			expressionName lineTo script(
				compilerName lineTo script(
					converter.scriptingLine,
					compiled.scriptingLine))

	fun process(compiled: Compiled) =
		compiled.resolve.let {
			lineConverterOrNull?.convert(it) ?: Compiler(converter, lineConverterOrNull, it)
		}

	fun process(typed: ExpressionTyped) =
		process(compiled(compiled.context, typed))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		when (name) {
			doName -> beginApply
			asName -> beginAs
			defineName -> beginDefine
			equalsName -> beginEquals
			givenName -> beginGiven
			functionName -> beginFunction
			inName -> beginIn
			ofName -> beginOf
			contentName -> beginContent
			previousName -> beginPrevious
			setName -> beginSet
			matchName -> beginMatch
			matchingName -> beginMatching
			else -> beginOther(name)
		}

	val end
		get() =
			converter.convert(compiled.typed)

	val beginApply: Processor<Token>
		get() =
			compiled
				.typed
				.type
				.contentOrNull
				?.arrowOrNull
				?.let { arrow ->
					Compiler(
						converter { typedParameter ->
							if (typedParameter.type != arrow.lhs) tracedError(
								mismatchName lineTo script(
									expectedName lineTo script(arrow.lhs.scriptingLine),
									actualName lineTo script(typedParameter.type.scriptingLine)))
							else process(
								typed(
									compiled.typed.expression.plus(apply(typedParameter.expression).op),
									arrow.rhs))
						},
						null,
						compiled.begin)
				}
				?: tracedError(expectedName lineTo script(arrowName))

	val beginAs: Processor<Token>
		get() =
			Compiler(
				converter { plusAs(it) },
				null,
				compiled.begin)

	val beginDefine: Processor<Token>
		get() =
			DefineCompiler(
				converter { newContext -> process(compiled(newContext, expressionTyped())) },
				compiled.context,
				type())

	val beginEquals: Processor<Token>
		get() =
			Compiler(
				converter { plusEquals(it) },
				null,
				compiled.begin)

	val beginContent: Processor<Token>
		get() =
			Compiler(
				converter { plusContent(it) },
				null,
				compiled.begin)

	val beginFunction: Processor<Token>
		get() =
			if (!compiled.typed.isEmpty) beginOther(functionName)
			else FunctionCompiler(
				converter { typedFunction ->
					process(
						typed(
							expression(op(value(item(typedFunction.function)))),
							type(typedFunction.arrow)))
				},
				compiled.context,
				type(),
				null,
				null)

	val beginGiven: Processor<Token>
		get() =
			Compiler(
				converter { plusGiven(it) },
				null,
				compiled.begin)

	val beginMatching: Processor<Token>
		get() =
			Compiler(
				converter { plusSwitched(it) },
				null,
				compiled.begin)

	val beginIn: Processor<Token>
		get() =
			Compiler(
				converter { plusIn(it) },
				null,
				compiled(
					compiled.context.give(compiled.typed.type),
					expressionTyped()))

	val beginOf: Processor<Token>
		get() =
			TypeCompiler(
				converter { plusOf(it) },
				false,
				typeContext(compiled.context),
				type())

	val beginPrevious: Processor<Token>
		get() =
			Compiler(
				converter { plusPrevious(it) },
				null,
				compiled.begin)

	val beginSet: Processor<Token>
		get() =
			lineCompiler(
				converter { plusSet(it) },
				compiled.context)

	val beginMatch: Processor<Token>
		get() =
			compiled
				.typed
				.type
				.linkOrNull
				?.let { link ->
					link
						.item
						.line
						.let { line ->
							line
								.rhs
								.optionsOrNull
								?.let { options ->
									switchCompiler(
										converter { plus(it) },
										compiled.context,
										options().plusReversed(options),
										typed(
											switch(),
											compiled.typed.type))
								}
						}
						?: tracedError(expectedName lineTo script(optionsName))
				} ?: tracedError(emptyName lineTo script())

	fun beginOther(name: String): Processor<Token> =
		Compiler(
			converter { plus(name lineTo it) },
			null,
			compiled.begin)
}

fun Converter<ExpressionTyped, Token>.compiler(context: Context = context()) =
	Compiler(this, null, compiled(context))

fun compiler() = Compiler(errorConverter(), null, compiled())

fun Compiler.plus(line: ExpressionTypedLine) =
	plusOther(line)

// TODO: Support multiple names.
fun Compiler.plusAs(rhs: ExpressionTyped) =
	rhs
		.type
		.onlyNameOrNull
		?.let { name ->
			process(
				typed(
					compiled.typed.expression.plus(wrap(name).op),
					type(name lineTo compiled.typed.type)))
		}
		?: tracedError(expectedName lineTo script(nameName))

fun Compiler.plusContent(rhs: ExpressionTyped) =
	if (!compiled.typed.type.isEmpty) tracedError()
	else rhs.contentOrNull?.let { process(it) } ?: tracedError()

fun Compiler.plusEquals(rhs: ExpressionTyped) =
	if (compiled.typed.type != rhs.type) tracedError(
		mismatchName lineTo script(
			expectedName lineTo script(compiled.typed.type.scriptingLine),
			actualName lineTo script(rhs.type.scriptingLine)))
	else process(
		typed(
			compiled.typed.expression.plus(op(leo13.expression.equals(rhs.expression))),
			type(booleanTypeLine)))

fun Compiler.plusIn(rhs: ExpressionTyped) =
	process(compiled.typed.plusIn(rhs))

fun Compiler.plusGiven(rhs: ExpressionTyped) =
	if (!compiled.typed.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else process(typed(expression(given.op), compiled.context.givenType))

fun Compiler.plusSwitched(rhs: ExpressionTyped) =
	if (!compiled.typed.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else process(typed(expression(switched.op), compiled.context.matchingType))

fun Compiler.plusPrevious(rhs: ExpressionTyped) =
	if (!compiled.typed.type.isEmpty) tracedError()
	else rhs.previousOrNull?.let { process(it) } ?: tracedError()

fun Compiler.plusOf(rhs: Type) =
	if (rhs.contains(compiled.typed.type)) process(typed(compiled.typed.expression, rhs))
	else tracedError(notName lineTo script(
		rhs.scriptingLine,
		containsName lineTo script(compiled.typed.type.scriptingLine)))

fun Compiler.plus(typedSwitch: SwitchTyped) =
	process(compiled.typed.plus(typedSwitch))

fun Compiler.plusSet(lineStack: Stack<ExpressionTypedLine>) =
	process(compiled.typed.fold(lineStack.reverse) { plusSet(it) })

fun ExpressionTyped.plusSet(line: ExpressionTypedLine): ExpressionTyped =
	type.getOrNull(line.name)
		?.let { lineRhsType ->
			if (type(line.name lineTo line.rhs.type) != lineRhsType)
				tracedError(mismatchName lineTo script(
					expectedName lineTo script(lineRhsType.scriptingLine),
					actualName lineTo script(type(line.name lineTo line.rhs.type).scriptingLine)))
			else typed(
				expression.plus(set(line.expressionLine).op),
				type)
		} ?: tracedError(setName lineTo script())

fun Compiler.plusOther(line: ExpressionTypedLine) =
	plusGetOrNull(line) ?: append(line)

fun Compiler.plusGetOrNull(line: ExpressionTypedLine) =
	ifOrNull(line.rhs.type.isEmpty) {
		compiled.typed.getOrNull(line.name)?.run { process(this) }
	}

fun Compiler.append(line: ExpressionTypedLine) =
	process(compiled.typed.plus(compiled.context.typeLines.resolve(line)))
