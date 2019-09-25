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
	val converter: Converter<TypedExpression, Token>,
	val processor: Processor<Compiled>,
	val compiled: Compiled) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			expressionName lineTo script(
				compilerName lineTo script(
					converter.scriptingLine,
					processor.scriptingLine,
					compiled.scriptingLine))

	fun process(compiled: Compiled) =
		Compiler(converter, processor.process(compiled), compiled)

	fun process(typed: TypedExpression) =
		process(compiled(compiled.context, typed))

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
			converter.convert(compiled.typed)

	val beginApply: Processor<Token>
		get() =
			compiled
				.typed
				.type
				.arrowOrNull
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
						voidProcessor(),
						compiled.begin)
				}
				?: tracedError(expectedName lineTo script(arrowName))

	val beginAs: Processor<Token>
		get() =
			Compiler(
				converter { plusAs(it) },
				voidProcessor(),
				compiled.begin)

	val beginDefine: Processor<Token>
		get() =
			DefineCompiler(
				converter { newContext -> process(compiled(newContext, typed())) },
				compiled.context,
				type())

	val beginContent: Processor<Token>
		get() =
			Compiler(
				converter { plusContent(it) },
				voidProcessor(),
				compiled.begin)

	val beginFunction: Processor<Token>
		get() =
			if (!compiled.typed.isEmpty)
				tracedError<Processor<Token>>(notName lineTo script(expectedName lineTo script(functionName)))
			else FunctionCompiler(
				converter { typedFunction ->
					process(
						typed(
							expression(op(value(item(typedFunction.function)))),
							type(typedFunction.arrow)))
				},
				compiled.context,
				type(),
				null)

	val beginGiven: Processor<Token>
		get() =
			Compiler(
				converter { plusGiven(it) },
				voidProcessor(),
				compiled.begin)

	val beginMatching: Processor<Token>
		get() =
			Compiler(
				converter { plusSwitched(it) },
				voidProcessor(),
				compiled.begin)

	val beginIn: Processor<Token>
		get() =
			Compiler(
				converter { plusIn(it) },
				voidProcessor(),
				compiled(
					compiled.context.give(compiled.typed.type),
					typed()))

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
				voidProcessor(),
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

	val beginType: Processor<Token>
		get() =
			Compiler(
				converter { plusType(it) },
				voidProcessor(),
				compiled.begin)

	val beginCompiler: Processor<Token>
		get() =
			Compiler(
				converter { plusCompiler(it) },
				voidProcessor(),
				compiled.begin)

	fun beginOther(name: String): Processor<Token> =
		Compiler(
			converter { plus(name lineTo it) },
			voidProcessor(),
			compiled.begin)
}

fun Converter<TypedExpression, Token>.compiler() =
	Compiler(this, voidProcessor(), compiled())

fun Processor<Compiled>.compiler() =
	Compiler(errorConverter(), this, compiled())

fun compiler() = Compiler(errorConverter(), voidProcessor(), compiled())

fun Compiler.plus(line: TypedExpressionLine): Compiler =
	plusOther(line)

// TODO: Support multiple names.
fun Compiler.plusAs(rhs: TypedExpression): Compiler =
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

fun Compiler.plusContent(rhs: TypedExpression): Compiler =
	if (!compiled.typed.type.isEmpty) tracedError()
	else rhs.contentOrNull?.let { process(it) } ?: tracedError()

fun Compiler.plusIn(rhs: TypedExpression): Compiler =
	process(compiled.typed.plusIn(rhs))

fun Compiler.plusGiven(rhs: TypedExpression): Compiler =
	if (!compiled.typed.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else process(typed(expression(given.op), compiled.context.givenType))

fun Compiler.plusSwitched(rhs: TypedExpression): Compiler =
	if (!compiled.typed.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else process(typed(expression(switched.op), compiled.context.matchingType))

fun Compiler.plusPrevious(rhs: TypedExpression): Compiler =
	if (!compiled.typed.type.isEmpty) tracedError()
	else rhs.previousOrNull?.let { process(it) } ?: tracedError()

fun Compiler.plusOf(rhs: Type): Compiler =
	if (rhs.contains(compiled.typed.type)) process(typed(compiled.typed.expression, rhs))
	else tracedError(notName lineTo script(
		rhs.scriptingLine,
		containsName lineTo script(compiled.typed.type.scriptingLine)))

fun Compiler.plus(typedSwitch: TypedSwitch): Compiler =
	process(compiled.typed.plus(typedSwitch))

fun Compiler.plusSet(lineStack: Stack<TypedExpressionLine>): Compiler =
	fold(lineStack.reverse) { plusSet(it) }

fun Compiler.plusSet(line: TypedExpressionLine): Compiler =
	compiled.typed.type.getOrNull(line.name)
		?.let { lineRhsType ->
			if (type(line.name lineTo line.rhs.type) != lineRhsType)
				tracedError(mismatchName lineTo script(
					expectedName lineTo script(lineRhsType.scriptingLine),
					actualName lineTo script(type(line.name lineTo line.rhs.type).scriptingLine)))
			else process(
				typed(
					compiled.typed.expression.plus(set(line.expressionLine).op),
					compiled.typed.type))
		} ?: tracedError(setName lineTo script())

fun Compiler.plusOther(line: TypedExpressionLine): Compiler =
	plusGetOrNull(line) ?: append(line)

fun Compiler.plusGetOrNull(line: TypedExpressionLine): Compiler? =
	ifOrNull(line.rhs.type.isEmpty) {
		compiled.typed.getOrNull(line.name)?.run { process(this) }
	}

fun Compiler.plusType(rhs: TypedExpression): Compiler =
	append(parentName lineTo typed(rhs.type.scriptingLine.rhs))

fun Compiler.plusCompiler(rhs: TypedExpression): Compiler =
	if (!rhs.type.isEmpty) tracedError(expectedName lineTo script(emptyName))
	else process(typed(script(scriptingLine)))

fun Compiler.append(line: TypedExpressionLine): Compiler =
	process(compiled.typed.plus(compiled.context.typeLines.resolve(line)))
