package leo14.untyped

import leo13.expectedName
import leo13.fold
import leo13.recursiveName
import leo13.reverse
import leo14.*

data class Resolver(
	val compiler: Compiler,
	val thunk: Thunk)

fun Context.eval(script: Script): Thunk =
	resolver().compile(script).thunk

fun Compiler.resolver(thunk: Thunk = thunk(value())) =
	Resolver(this, thunk)

fun Compiler.resolver(value: Value) =
	resolver(thunk(value))

fun Context.resolver(thunk: Thunk = thunk(value())) =
	compiler(this).resolver(thunk)

fun Context.resolver(value: Value) =
	compiler(this).resolver(value)

fun resolver(value: Value = value()) =
	context().resolver(value)

fun Resolver.apply(line: Line): Resolver =
	compiler.applyContext.resolve(this.thunk.plus(line))

fun Resolver.lazy(script: Script): Resolver =
	compiler.resolver(thunk(lazy(compiler.applyContext, script)))

fun Resolver.do_(script: Script): Resolver =
	set(
		compiler
			.applyContext
			.withGiven(thunk)
			.asLazy(script)
			.eval)

fun Resolver.recursively(script: Script): Resolver =
	compiler
		.applyContext
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
				.rhsOrNull(sequence.head.selectName)
				?.let { body ->
					set(
						compiler
							.applyContext
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
		thunk.script

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
		?: resolver(thunk)

fun Context.resolveContext(thunk: Thunk): Resolver? =
	apply(thunk)?.let { resolver(it) }

fun Context.resolveDefinitions(thunk: Thunk): Resolver? =
	compile(thunk)?.resolver()

fun Context.resolveCompile(thunk: Thunk): Resolver? =
	thunk.matchInfix(compileName) { lhs, rhs ->
		resolver(lhs).compile(rhs.script)
	}

fun Resolver.set(thunk: Thunk): Resolver =
	copy(thunk = thunk)

val Resolver.clear
	get() =
		set(thunk(value()))

fun Context.resolver(sequence: Sequence): Resolver =
	resolver(sequence.tail).apply(sequence.head)

fun Resolver.function(script: Script): Resolver =
	when (script) {
		is UnitScript -> apply(functionName lineTo value())
		is LinkScript -> apply(line(function(compiler.applyContext, script)))
	}

val Script.resolveRecursive: Script?
	get() =
		(this as? LinkScript)?.link?.let { link ->
			if (!link.lhs.isEmpty) null
			else (link.line as? FieldScriptLine)?.field?.let { field ->
				if (field.string == recursiveName) field.rhs
				else null
			}
		}

fun Resolver.assert(script: Script): Resolver =
	script
		.matchInfix(givesName) { lhs, rhs ->
			compiler.evalThunk(lhs).let { lhsEvaled ->
				compiler.evalThunk(rhs).let { rhsEvaled ->
					if (lhsEvaled != rhsEvaled) error(
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
	compiler
		.push(definition(rule(pattern(thunk), evalBody(script))))
		.resolver(value())

fun Resolver.compile(script: Script): Resolver =
	reader
		.fold(script.tokenStack.reverse) { write(it)!! }
		.run { this as UnquotedReader }
		.unquoted
		.resolver
