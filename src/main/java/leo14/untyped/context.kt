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

fun Context.applyFunction(program: Program): Program? =
	program.matchPrefix("function") { body ->
		body.scriptOrNull?.let { body ->
			program(value(function(this, body)))
		}
	}

fun Context.compile(program: Program): Context? =
	null
		?: compileIs(program)
		?: compileGives(program)
		?: compileSaveAs(program)

fun Context.compileGives(program: Program): Context? =
	program.matchInfix(givesName) { lhs, rhs ->
		rhs.scriptOrNull?.let { rhs ->
			push(Rule(Pattern(lhs), body(function(this, rhs))))
		}
	}

fun Context.compileIs(program: Program): Context? =
	program.matchInfix(isName) { lhs, rhs ->
		push(Rule(Pattern(lhs), body(rhs)))
	}

fun Context.compileSaveAs(program: Program): Context? =
	program.matchInfix("save", "as") { lhs, rhs ->
		push(Rule(Pattern(rhs), body(lhs)))
	}
