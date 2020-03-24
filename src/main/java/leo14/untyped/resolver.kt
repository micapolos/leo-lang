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
	compiler.resolver(
		function(
			compiler.applyContext,
			script,
			recursive = false)
			.apply(thunk))

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
		?: resolveSwitch(thunk)
		?: resolveStatic(thunk)

fun Context.resolveStatic(thunk: Thunk): Resolver =
	null
		?: resolveCompile(thunk)
		?: resolver(thunk)

fun Context.resolveContext(thunk: Thunk): Resolver? =
	apply(thunk)?.let { resolver(it) }

fun Context.resolveDefinitions(thunk: Thunk): Resolver? =
	compile(thunk)?.resolver()

fun Context.resolveSwitch(thunk: Thunk): Resolver? =
	null
//	thunk.value.resolveSwitchMatch?.let { switchMatch ->
//		resolver(switchMatch.param).eval(switchMatch.body)
//	}

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
		is LinkScript -> script.resolveRecursive
			?.let { recursiveScript ->
				apply(line(function(compiler.applyContext, recursiveScript, recursive = true)))
			}
			?: apply(line(function(compiler.applyContext, script, recursive = false)))
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
		.push(definition(rule(pattern(thunk), body(script))))
		.resolver(value())

fun Resolver.compile(script: Script): Resolver =
	reader
		.fold(script.tokenStack.reverse) { write(it)!! }
		.run { this as UnquotedReader }
		.unquoted
		.resolver
