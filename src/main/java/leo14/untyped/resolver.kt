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

fun Resolver.apply(value: Value): Resolver =
	compiler.applyContext.resolve(this.program.plus(value))

fun Resolver.lazy(script: Script): Resolver =
	compiler.resolver(thunk(lazy(compiler.applyContext, script)))

val Resolver.program
	get() =
		thunk.program

fun Resolver.append(value: Value): Resolver =
	set(this.program.plus(value))

fun Resolver.append(program: Program): Resolver =
	set(this.program.plus(program))

fun Context.resolve(program: Program): Resolver =
	null
		?: resolveContext(program)
		?: resolveDefinitions(program)
		?: resolveSwitch(program)
		?: resolveStatic(program)

fun Context.resolveStatic(program: Program): Resolver =
	null
		?: resolveCompile(program)
		?: resolver(program)

fun Context.resolveContext(program: Program): Resolver? =
	apply(program)?.let { resolver(it) }

fun Context.resolveDefinitions(program: Program): Resolver? =
	compile(program)?.resolver()

fun Context.resolveSwitch(program: Program): Resolver? =
	null
//	program.resolveSwitchMatch?.let { switchMatch ->
//		resolver(switchMatch.param).eval(switchMatch.body)
//	}

fun Context.resolveCompile(program: Program): Resolver? =
	program.matchInfix(compileName) { lhs, rhs ->
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
		is UnitScript -> apply(functionName valueTo program())
		is LinkScript -> script.resolveRecursive
			?.let { recursiveScript ->
				apply(value(function(compiler.applyContext, recursiveScript, recursive = true)))
			}
			?: apply(value(function(compiler.applyContext, script, recursive = false)))
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
						errorName valueTo
							lhs.program.plus(
								program(
									givesName valueTo lhsEvaled,
									expectedName valueTo rhsEvaled)))
					else this
				}
			}
		}
		?: append(assertName valueTo script.program)

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
