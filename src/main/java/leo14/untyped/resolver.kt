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

fun Compiler.resolver(thunk: Thunk = thunk(program())) =
	Resolver(this, thunk)

fun Compiler.resolver(program: Program) =
	resolver(thunk(program))

fun Context.resolver(thunk: Thunk = thunk(program())) =
	compiler(this).resolver(thunk)

fun Context.resolver(program: Program) =
	compiler(this).resolver(program)

fun resolver(program: Program = program()) =
	context().resolver(program)

fun Resolver.apply(line: Line): Resolver =
	compiler.applyContext.resolve(thunk(this.program.plus(line)))

fun Resolver.lazy(script: Script): Resolver =
	compiler.resolver(thunk(lazy(compiler.applyContext, script)))

val Resolver.program
	get() =
		thunk.program

val Resolver.printScript
	get() =
		thunk.script

fun Resolver.append(line: Line): Resolver =
	set(this.program.plus(line))

fun Resolver.append(thunk: Thunk): Resolver =
	set(this.program.plus(thunk.program))

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
//	program.resolveSwitchMatch?.let { switchMatch ->
//		resolver(switchMatch.param).eval(switchMatch.body)
//	}

fun Context.resolveCompile(thunk: Thunk): Resolver? =
	thunk.program.matchInfix(compileName) { lhs, rhs ->
		rhs.scriptOrNull?.let { code ->
			resolver(lhs).compile(code)
		}
	}

fun Resolver.set(program: Program): Resolver =
	copy(thunk = thunk(program))

val Resolver.clear
	get() =
		set(program())

fun Context.resolver(sequence: Sequence): Resolver =
	resolver(sequence.tail).apply(sequence.head)

fun Resolver.function(script: Script): Resolver =
	when (script) {
		is UnitScript -> apply(functionName lineTo program())
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
			compiler.eval(lhs).let { lhsEvaled ->
				compiler.eval(rhs).let { rhsEvaled ->
					if (lhsEvaled != rhsEvaled) error(
						errorName lineTo
							lhs.program.plus(
								program(
									givesName lineTo lhsEvaled,
									expectedName lineTo rhsEvaled)))
					else this
				}
			}
		}
		?: append(assertName lineTo script.program)

fun Resolver.does(script: Script): Resolver =
	compiler
		.push(definition(rule(pattern(program), body(script))))
		.resolver(program())

fun Resolver.compile(script: Script): Resolver =
	reader
		.fold(script.tokenStack.reverse) { write(it)!! }
		.run { this as UnquotedReader }
		.unquoted
		.resolver
