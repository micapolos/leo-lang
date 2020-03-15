package leo14.untyped

import leo.base.fold

sealed class Context
object EmptyContext : Context()
data class NonEmptyContext(val parentContext: Context, val lastRule: Rule) : Context()

fun context() = EmptyContext as Context

fun context(rule: Rule, vararg rules: Rule) =
	context().push(rule).fold(rules) { push(it) }

fun Context.push(rule: Rule): Context =
	NonEmptyContext(this, rule)

fun Context.apply(program: Program): Program? =
	null
		?: applyRules(program)
		?: applyStatic(program)
		?: program.resolve

fun Context.applyRules(program: Program): Program? =
	when (this) {
		EmptyContext -> null
		is NonEmptyContext -> lastRule.apply(program) ?: parentContext.applyRules(program)
	}

fun Context.applyStatic(program: Program): Program? =
	null
		?: applyFunction(program)
		?: applyEval(program)

fun Context.applyFunction(program: Program): Program? =
	program.matchPrefix("function") { body ->
		program(value(function(this, body)))
	}

fun Context.applyEval(program: Program): Program? =
	program.matchPostfix("eval") { lhs ->
		eval(lhs)
	}

fun Context.compile(program: Program): Context? =
	null
		?: compileGives(program)
		?: compileDoes(program)

fun Context.compileDoes(program: Program): Context? =
	program.matchInfix("does") { lhs, rhs ->
		push(Rule(Pattern(lhs), body(function(this, rhs))))
	}

fun Context.compileGives(program: Program): Context? =
	program.matchInfix("gives") { lhs, rhs ->
		push(Rule(Pattern(lhs), body(rhs)))
	}

fun Context.eval(program: Program) =
	resolver().tokenReader().append(program).resolver.program