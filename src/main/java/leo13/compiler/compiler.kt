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
	val converter: Converter<Compiled, Token>,
	val context: Context,
	val compiled: Compiled
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
					compiled.scriptingLine))

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
			converter.convert(compiled)

	val beginApply: Processor<Token>
		get() =
			compiled
				.type.arrowOrNull
				?.let { arrow ->
					compiler(
						converter { parameterCompiled ->
							if (parameterCompiled.type != arrow.lhs) tracedError(
								mismatchName lineTo script(
									expectedName lineTo script(arrow.lhs.scriptingLine),
									actualName lineTo script(parameterCompiled.type.scriptingLine)))
							else set(
								compiled(
									compiled.expression.plus(apply(parameterCompiled.expression).op),
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
			if (!compiled.isEmpty)
				tracedError<Processor<Token>>(notName lineTo script(expectedName lineTo script(functionName)))
			else FunctionCompiler(
				converter { compiledFunction ->
					set(
						compiled(
							expression(op(value(item(compiledFunction.function)))),
							type(compiledFunction.arrow)))
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
				context.give(compiled.type))

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
			compiled
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
										compiled(
											switch(),
											compiled.type))
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
	copy(compiled = context.functions.resolve(compiled))

fun Compiler.plus(line: CompiledLine): Compiler =
	plusOther(line)

// TODO: Support multiple names.
fun Compiler.plusAs(rhs: Compiled): Compiler =
	rhs
		.type
		.onlyNameOrNull
		?.let { name ->
			set(
				compiled(
					compiled.expression.plus(wrap(name).op),
					type(name lineTo compiled.type)))
		}
		?: tracedError(expectedName lineTo script(nameName))

fun Compiler.plusContent(rhs: Compiled): Compiler =
	if (!compiled.type.isEmpty) tracedError()
	else rhs.contentOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusIn(rhs: Compiled): Compiler =
	set(compiled.plusIn(rhs))

fun Compiler.plusGiven(rhs: Compiled): Compiler =
	if (!compiled.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else set(compiled(expression(given.op), context.givenType))

fun Compiler.plusSwitched(rhs: Compiled): Compiler =
	if (!compiled.type.isEmpty || !rhs.type.isEmpty) tracedError()
	else set(compiled(expression(switched.op), context.matchingType))

fun Compiler.plusPrevious(rhs: Compiled): Compiler =
	if (!compiled.type.isEmpty) tracedError()
	else rhs.previousOrNull?.let { set(it) } ?: tracedError()

fun Compiler.plusOf(rhs: Type): Compiler =
	if (rhs.contains(compiled.type)) set(compiled(compiled.expression, rhs))
	else tracedError(notName lineTo script(
		rhs.scriptingLine,
		containsName lineTo script(compiled.type.scriptingLine)))

fun Compiler.plus(switchCompiled: SwitchCompiled): Compiler =
	set(compiled.plus(switchCompiled))

fun Compiler.plusSet(lineStack: Stack<CompiledLine>): Compiler =
	fold(lineStack.reverse) { plusSet(it) }

fun Compiler.plusSet(line: CompiledLine): Compiler =
	compiled.type.getOrNull(line.name)
		?.let { lineRhsType ->
			if (type(line.name lineTo line.rhs.type) != lineRhsType)
				tracedError(mismatchName lineTo script(
					expectedName lineTo script(lineRhsType.scriptingLine),
					actualName lineTo script(type(line.name lineTo line.rhs.type).scriptingLine)))
			else set(
				compiled(
					compiled.expression.plus(set(line.expressionLine).op),
					compiled.type))
		} ?: tracedError(setName lineTo script())

fun Compiler.plusOther(line: CompiledLine): Compiler =
	plusGetOrNull(line) ?: append(line)

fun Compiler.plusGetOrNull(line: CompiledLine): Compiler? =
	ifOrNull(line.rhs.type.isEmpty) {
		compiled.getOrNull(line.name)?.run { set(this) }
	}

fun Compiler.plusType(rhs: Compiled): Compiler =
	append(parentName lineTo compiled(rhs.type.scriptingLine.rhs))

fun Compiler.plusCompiler(rhs: Compiled): Compiler =
	if (!rhs.type.isEmpty) tracedError(expectedName lineTo script(emptyName))
	else set(compiled(script(scriptingLine)))

fun Compiler.append(line: CompiledLine): Compiler =
	set(compiled.plus(context.typeLines.resolve(line)))
