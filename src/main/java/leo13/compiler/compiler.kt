package leo13.compiler

import leo.base.ifOrNull
import leo13.*
import leo13.expression.*
import leo13.type.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.value.item
import leo13.value.value

data class Compiler(
	val converter: Converter<TypedExpression, Token>,
	val context: Context,
	val typedExpression: TypedExpression
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			expressionName lineTo script(
				compilerName lineTo script(
					converter.scriptingLine,
					context.scriptingLine,
					typedExpression.scriptingLine))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		when (name) {
			applyName -> beginApply
			asName -> beginAs
			defineName -> beginDefine
			givenName -> beginGiven
			functionName -> beginFunction
			inName -> beginIn
			ofName -> beginOf
			contentName -> beginContent
			previousName -> beginPrevious
			setName -> beginSet
			matchName -> beginMatch
			matchingName -> beginMatching
			typeName -> beginType
			compilerName -> beginCompiler
			else -> beginOther(name)
		}

	val end
		get() =
			converter.convert(typedExpression)

	val beginApply: Processor<Token>
		get() =
			typedExpression
				.type.arrowOrNull
				?.let { arrow ->
					compiler(
						converter { typedParameter ->
							if (typedParameter.type != arrow.lhs) tracedError(
								mismatchName lineTo script(
									expectedName lineTo script(arrow.lhs.scriptingLine),
									actualName lineTo script(typedParameter.type.scriptingLine)))
							else set(
								typed(
									typedExpression.expression.plus(apply(typedParameter.expression).op),
									arrow.rhs))
						},
						context)
				}
				?: tracedError(expectedName lineTo script(arrowName))

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
				type())

	val beginContent: Processor<Token>
		get() =
			compiler(
				converter { plusContent(it) },
				context)

	val beginFunction: Processor<Token>
		get() =
			if (!typedExpression.isEmpty)
				tracedError<Processor<Token>>(notName lineTo script(expectedName lineTo script(functionName)))
			else FunctionCompiler(
				converter { typedFunction ->
					set(
						typed(
							expression(op(value(item(typedFunction.function)))),
							type(typedFunction.arrow)))
				},
				context,
				type(),
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
				context.give(typedExpression.type))

	val beginOf: Processor<Token>
		get() =
			TypeCompiler(
				converter { plusOf(it) },
				false,
				typeContext(context),
				type())

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
			typedExpression
				.type.linkOrNull
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
										context,
										options().plusReversed(options),
										typed(
											switch(),
											typedExpression.type))
								}
						}
						?: tracedError(expectedName lineTo script(optionsName))
				} ?: tracedError(emptyName lineTo script())

	val beginType: Processor<Token>
		get() =
			compiler(
				converter { plusType(it) },
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
	compiler(errorConverter(), context(), typed())

fun Converter<TypedExpression, Token>.compiler() =
	compiler(this, context(), typed())

fun compiler(
	converter: Converter<TypedExpression, Token>,
	context: Context,
	typedExpression: TypedExpression = typed()) =
	Compiler(converter, context, typedExpression)

fun Compiler.set(context: Context) =
	copy(context = context)

fun Compiler.set(typedExpression: TypedExpression) =
	copy(typedExpression = context.functions.resolve(typedExpression))

fun Compiler.plus(line: TypedExpressionLine): Compiler =
	plusOther(line)

// TODO: Support multiple names.
fun Compiler.plusAs(rhs: TypedExpression): Compiler =
	rhs
		.type
		.onlyNameOrNull
		?.let { name ->
			set(
				typed(
					typedExpression.expression.plus(wrap(name).op),
					type(name lineTo typedExpression.type)))
		}
		?: tracedError(expectedName lineTo script(nameName))

fun Compiler.plusContent(rhs: TypedExpression): Compiler =
	if (!typedExpression.type.isEmpty) tracedError()
	else rhs.contentOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusIn(rhs: TypedExpression): Compiler =
	set(typedExpression.plusIn(rhs))

fun Compiler.plusGiven(rhs: TypedExpression): Compiler =
	if (!typedExpression.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else set(typed(expression(given.op), context.givenType))

fun Compiler.plusSwitched(rhs: TypedExpression): Compiler =
	if (!typedExpression.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else set(typed(expression(switched.op), context.matchingType))

fun Compiler.plusPrevious(rhs: TypedExpression): Compiler =
	if (!typedExpression.type.isEmpty) tracedError()
	else rhs.previousOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusOf(rhs: Type): Compiler =
	if (rhs.contains(typedExpression.type)) set(typed(typedExpression.expression, rhs))
	else tracedError(notName lineTo script(
		rhs.scriptingLine,
		containsName lineTo script(typedExpression.type.scriptingLine)))

fun Compiler.plus(typedSwitch: TypedSwitch): Compiler =
	set(typedExpression.plus(typedSwitch))

fun Compiler.plusSet(lineStack: Stack<TypedExpressionLine>): Compiler =
	fold(lineStack.reverse) { plusSet(it) }

fun Compiler.plusSet(line: TypedExpressionLine): Compiler =
	typedExpression.type.getOrNull(line.name)
		?.let { lineRhsType ->
			if (type(line.name lineTo line.rhs.type) != lineRhsType)
				tracedError(mismatchName lineTo script(
					expectedName lineTo script(lineRhsType.scriptingLine),
					actualName lineTo script(type(line.name lineTo line.rhs.type).scriptingLine)))
			else set(
				typed(
					typedExpression.expression.plus(set(line.expressionLine).op),
					typedExpression.type))
		} ?: tracedError(setName lineTo script())

fun Compiler.plusOther(line: TypedExpressionLine): Compiler =
	plusGetOrNull(line) ?: append(line)

fun Compiler.plusGetOrNull(line: TypedExpressionLine): Compiler? =
	ifOrNull(line.rhs.type.isEmpty) {
		typedExpression.getOrNull(line.name)?.run { set(this) }
	}

fun Compiler.plusType(rhs: TypedExpression): Compiler =
	append(parentName lineTo typed(rhs.type.scriptingLine.rhs))

fun Compiler.plusCompiler(rhs: TypedExpression): Compiler =
	if (!rhs.type.isEmpty) tracedError(expectedName lineTo script(emptyName))
	else set(typed(script(scriptingLine)))

fun Compiler.append(line: TypedExpressionLine): Compiler =
	set(typedExpression.plus(context.typeLines.resolve(line)))
