package leo14.untyped

import leo13.expectedName
import leo13.fold
import leo13.reverse
import leo14.*

data class Resolver(
	val scope: Scope,
	val thunk: Thunk)

fun Scope.eval(script: Script): Thunk =
	resolver().compile(script).thunk

fun Scope.resolver(thunk: Thunk = thunk(value())) =
	Resolver(this, thunk)

fun Resolver.apply(line: Line): Resolver =
	scope.apply(thunk.sequenceTo(line))

fun Resolver.lazy(script: Script): Resolver =
	scope.resolver(thunk(lazy(scope, script)))

tailrec fun Resolver.do_(script: Script): Resolver {
	val done = scope.bind(thunk).evaluate(script)
	val repeatOrNull = done.matchPrefix(repeatName) { it }
	return if (repeatOrNull == null) set(done)
	else set(repeatOrNull).do_(script)
}

fun Resolver.match(script: Script): Resolver =
	thunk.matchField { structField ->
		structField.thunk.value.sequenceOrNull?.let { sequence ->
			script
				.rhsOrNull(sequence.lastLine.selectName)
				?.let { body ->
					set(
						scope
							.push(
								definition(
									thunk(value(matchingName))
										.bindingTo(thunk(value(matchingName lineTo value(sequence))))))
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
		scope.reflect(thunk)

fun Resolver.append(line: Line): Resolver =
	set(this.thunk.plus(line))

fun Resolver.append(thunk: Thunk): Resolver =
	set(this.thunk.plus(thunk))

fun Scope.resolve(thunk: Thunk): Resolver =
	null
		?: resolveContext(thunk)
		?: resolveDefinitions(thunk)
		?: resolveStatic(thunk)

fun Scope.resolveStatic(thunk: Thunk): Resolver =
	null
		?: resolveCompile(thunk)
		?: resolveScope(thunk)
		?: resolveEvaluate(thunk)
		?: resolveScriptText(thunk)
		?: resolveLeoScript(thunk)
		?: resolver(thunk)

fun Scope.resolveContext(thunk: Thunk): Resolver? =
	apply(thunk)?.let { resolver(it) }

fun Scope.resolver(applied: Applied): Resolver =
	when (applied) {
		is ThunkApplied -> resolver(applied.thunk)
		is ScriptApplied -> resolver().compile(applied.script)
	}

fun Scope.resolveDefinitions(thunk: Thunk): Resolver? =
	compile(thunk)?.resolver()

fun Scope.resolveCompile(thunk: Thunk): Resolver? =
	thunk.matchPrefix(compileName) { lhs ->
		resolver(lhs).compile
	}

fun Scope.resolveScope(thunk: Thunk): Resolver? =
	thunk.matchInfix(scopeName) { lhs, rhs ->
		lhs.matchEmpty {
			rhs.matchEmpty {
				resolver(thunk(doingScript.value))
			}
		}
	}

fun Scope.resolveEvaluate(thunk: Thunk): Resolver? =
	thunk.matchPrefix(evaluateName) { rhs ->
		resolver(thunk(value())).evaluate(rhs.value.script)
	}

fun Scope.resolveScriptText(thunk: Thunk): Resolver? =
	thunk.matchPrefix(textName) { rhs ->
		rhs.matchPrefix(scriptName) { rhs ->
			resolver(thunk(value(literal(reflect(rhs).leoString))))
		}
	}

fun Scope.resolveLeoScript(thunk: Thunk): Resolver? =
	thunk.matchPrefix(scriptName) { rhs ->
		rhs.matchPrefix(leoName) { rhs ->
			resolver(thunk(value(reflect(rhs).reflectLine.valueLine)))
		}
	}

fun Resolver.set(thunk: Thunk): Resolver =
	copy(thunk = thunk)

val Resolver.clear
	get() =
		set(thunk(value()))

fun Scope.resolver(sequence: Sequence): Resolver =
	resolver(sequence.previousThunk).apply(sequence.lastLine)

fun Resolver.doing(script: Script): Resolver =
	when (script) {
		is UnitScript -> apply(doingName lineTo value())
		is LinkScript -> apply(line(doing(scope, script)))
	}

fun Resolver.assert(script: Script): Resolver =
	script
		.matchInfix(givesName) { lhs, rhs ->
			scope.evaluate(lhs).let { lhsEvaled ->
				scope.evaluate(rhs).let { rhsEvaled ->
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
	scope
		.push(definition(rule(pattern(thunk), doesBody(script))))
		.resolver(emptyThunk)

fun Resolver.expands(script: Script): Resolver =
	scope
		.push(definition(rule(pattern(thunk), expandsBody(script))))
		.resolver(emptyThunk)

val Resolver.compile: Resolver
	get() =
		clear.compile(scope.reflect(thunk))

fun Resolver.compile(script: Script): Resolver =
	reader
		.fold(script.tokenStack.reverse) { write(it)!! }
		.run { this as UnquotedReader }
		.unquoted
		.resolver

fun Resolver.evaluate(script: Script): Resolver =
	compile(script).thunk.let { scope.resolver(it) }
