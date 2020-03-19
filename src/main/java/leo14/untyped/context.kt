package leo14.untyped

import leo.base.fold
import leo13.fold
import leo13.reverse
import leo14.tokenStack

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
		?: compileIs(program)
		?: compileDoes(program)
		?: compileSaveAs(program)

fun Context.compileDoes(program: Program): Context? =
	program.matchInfix("does") { lhs, rhs ->
		push(Rule(Pattern(lhs), body(function(this, rhs))))
	}

fun Context.compileIs(program: Program): Context? =
	program.matchInfix("is") { lhs, rhs ->
		push(Rule(Pattern(lhs), body(rhs)))
	}

fun Context.compileSaveAs(program: Program): Context? =
	program.matchInfix("save", "as") { lhs, rhs ->
		push(Rule(Pattern(rhs), body(lhs)))
	}

fun Context.eval(program: Program): Program =
	(null as TokenizerParent?)
		.tokenizer(environment.evaluator(program()))
		.fold(program.script.tokenStack.reverse) { write(it)!! }
		.evaluator
		.program