package leo14.untyped

import leo13.contextName
import leo13.expectedName
import leo13.fold
import leo13.reverse
import leo14.*

data class Resolver(
	val context: Context,
	val thunk: Thunk)

fun Context.eval(script: Script): Thunk =
	resolver().compile(script).thunk

fun Context.resolver(thunk: Thunk = thunk(value())) =
	Resolver(this, thunk)

fun Resolver.apply(line: Line): Resolver =
	context.resolve(this.thunk.plus(line))

fun Resolver.lazy(script: Script): Resolver =
	context.resolver(thunk(lazy(context, script)))

fun Resolver.do_(script: Script): Resolver =
	set(
		context
			.withGiven(thunk)
			.asLazy(script)
			.eval)

fun Resolver.recursively(script: Script): Resolver =
	context
		.push(
			rule(
				pattern(thunk(value(anythingName lineTo value(), recurseName lineTo value()))),
				recurseBody(script(recursivelyName lineTo script))))
		.resolver(thunk)
		.compile(script)

fun Resolver.match(script: Script): Resolver =
	thunk.matchField { structField ->
		structField.thunk.value.sequenceOrNull?.let { sequence ->
			script
				.rhsOrNull(sequence.lastLine.selectName)
				?.let { body ->
					set(
						context
							.push(
								rule(
									pattern(thunk(value(matchingName))),
									body(thunk(value(matchingName lineTo value(sequence))))))
							.asLazy(body)
							.eval)
				}
			// TODO: any: case
		}
	} ?: append(matchName lineTo script.value)

val Resolver.value
	get() =
		thunk.value

val Resolver.printScript
	get() =
		context.reflect(thunk)

fun Resolver.append(line: Line): Resolver =
	set(this.thunk.plus(line))

fun Resolver.append(thunk: Thunk): Resolver =
	set(this.thunk.plus(thunk))

fun Context.resolve(thunk: Thunk): Resolver =
	null
		?: resolveContext(thunk)
		?: resolveDefinitions(thunk)
		?: resolveStatic(thunk)

fun Context.resolveStatic(thunk: Thunk): Resolver =
	null
		?: resolveCompile(thunk)
		?: resolveContextName(thunk)
		?: resolveEvaluate(thunk)
		?: resolver(thunk)

fun Context.resolveContext(thunk: Thunk): Resolver? =
	apply(thunk)?.let { resolver(it) }

fun Context.resolver(applied: Applied): Resolver =
	when (applied) {
		is ThunkApplied -> resolver(applied.thunk)
		is ScriptApplied -> resolver().compile(applied.script)
	}

fun Context.resolveDefinitions(thunk: Thunk): Resolver? =
	compile(thunk)?.resolver()

fun Context.resolveCompile(thunk: Thunk): Resolver? =
	thunk.matchPostfix(compileName) { lhs ->
		resolver(lhs).compile
	}

fun Context.resolveContextName(thunk: Thunk): Resolver? =
	thunk.matchInfix(contextName) { lhs, rhs ->
		lhs.matchEmpty {
			rhs.matchEmpty {
				resolver(thunk(functionScript.value))
			}
		}
	}

fun Context.resolveEvaluate(thunk: Thunk): Resolver? =
	thunk.matchPostfix(evaluateName) { lhs ->
		resolver(thunk(value())).evaluate(lhs.value.script)
	}

fun Resolver.set(thunk: Thunk): Resolver =
	copy(thunk = thunk)

val Resolver.clear
	get() =
		set(thunk(value()))

fun Context.resolver(sequence: Sequence): Resolver =
	resolver(sequence.previousThunk).apply(sequence.lastLine)

fun Resolver.function(script: Script): Resolver =
	when (script) {
		is UnitScript -> apply(functionName lineTo value())
		is LinkScript -> apply(line(function(context, script)))
	}

fun Resolver.assert(script: Script): Resolver =
	script
		.matchInfix(givesName) { lhs, rhs ->
			context.evaluate(lhs).let { lhsEvaled ->
				context.evaluate(rhs).let { rhsEvaled ->
					if (lhsEvaled != rhsEvaled) throw AssertionError(
						errorName lineTo
							lhs.value.plus(
								value(
									givesName lineTo lhsEvaled,
									expectedName lineTo rhsEvaled)))
					else this
				}
			}
		}
		?: append(assertName lineTo script.value)

fun Resolver.does(script: Script): Resolver =
	context
		.push(definition(rule(pattern(thunk), evalBody(script))))
		.resolver(emptyThunk)

fun Resolver.writes(script: Script): Resolver =
	context
		.push(definition(rule(pattern(thunk), compileBody(script))))
		.resolver(emptyThunk)

val Resolver.compile: Resolver
	get() =
		clear.compile(context.reflect(thunk))

fun Resolver.compile(script: Script): Resolver =
	reader
		.fold(script.tokenStack.reverse) { write(it)!! }
		.run { this as UnquotedReader }
		.unquoted
		.resolver

fun Resolver.evaluate(script: Script): Resolver =
	context.resolver(
		reader
			.fold(script.tokenStack.reverse) { write(it)!! }
			.run { this as UnquotedReader }
			.unquoted
			.resolver
			.thunk)

tailrec fun Resolver.loop(script: Script): Resolver =
	evaluate(script).loop(script)