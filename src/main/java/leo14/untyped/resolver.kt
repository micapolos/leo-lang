package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.LinkScript
import leo14.Script
import leo14.UnitScript
import leo14.tokenStack

data class Resolver(
	val compiler: Compiler,
	val program: Program)

fun Compiler.resolver(program: Program = program()) =
	Resolver(this, program)

fun Context.resolver(program: Program = program()) =
	compiler(this).resolver(program)

fun resolver(program: Program = program()) =
	context().resolver(program)

fun Resolver.apply(value: Value): Resolver =
	compiler.applyContext.resolve(this.program.plus(value))

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
	copy(program = program)

val Resolver.clear
	get() =
		set(program())

fun Context.resolver(sequence: Sequence): Resolver =
	resolver(sequence.tail).apply(sequence.head)

fun Resolver.function(script: Script): Resolver =
	when (script) {
		is UnitScript -> apply(functionName valueTo program())
		is LinkScript -> apply(value(function(compiler.applyContext, script)))
	}

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