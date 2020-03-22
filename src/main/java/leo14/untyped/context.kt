package leo14.untyped

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Context
data class EmptyContext(val empty: Empty) : Context()
data class LinkContext(val link: ContextLink) : Context()

data class ContextLink(val context: Context, val definition: Definition)

infix fun Context.linkTo(definition: Definition) = ContextLink(this, definition)
infix fun Context.linkTo(rule: Rule) = linkTo(definition(rule))

fun context() = EmptyContext(empty) as Context
fun context(link: ContextLink): Context = LinkContext(link)

fun context(rule: Rule, vararg rules: Rule) =
	context().push(rule).fold(rules) { push(it) }

fun Context.push(definition: Definition): Context =
	context(this linkTo definition)

fun Context.push(rule: Rule): Context =
	context(this linkTo rule)

fun Context.apply(program: Program): Program? =
	null
		?: applyRules(program)
		?: applyStatic(program)
		?: program.resolve

fun Context.applyRules(program: Program): Program? =
	when (this) {
		is EmptyContext -> null
		is LinkContext -> link.applyRules(program)
	}

fun ContextLink.applyRules(program: Program): Program? =
	definition.apply(context, program) ?: context.applyRules(program)

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
	program.matchInfix(doesName) { lhs, rhs ->
		rhs.scriptOrNull?.let { script ->
			push(Rule(Pattern(lhs), body(script)))
		}
	}

fun Context.compileIs(program: Program): Context? =
	program.matchInfix(givesName) { lhs, rhs ->
		push(Rule(Pattern(lhs), body(rhs)))
	}

fun Context.compileSaveAs(program: Program): Context? =
	program.matchInfix("save", "as") { lhs, rhs ->
		push(Rule(Pattern(rhs), body(lhs)))
	}
