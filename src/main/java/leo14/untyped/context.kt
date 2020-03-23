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

fun Context.apply(program: Program): Thunk? =
	null
		?: applyRules(program)
		?: program.resolve?.let(::thunk)

fun Context.applyRules(program: Program): Thunk? =
	when (this) {
		is EmptyContext -> null
		is LinkContext -> link.applyRules(program)
	}

fun ContextLink.applyRules(program: Program): Thunk? =
	definition.apply(context, program) ?: context.applyRules(program)

fun Context.compile(program: Program): Context? =
	null
		?: compileDoes(program)
		?: compileGives(program)
		?: compileAs(program)

fun Context.compileDoes(program: Program): Context? =
	program.matchInfix(doesName) { lhs, rhs ->
		rhs.scriptOrNull?.let { script ->
			push(Rule(Pattern(lhs), body(script)))
		}
	}

fun Context.compileGives(program: Program): Context? =
	program.matchInfix(givesName) { lhs, rhs ->
		push(Rule(Pattern(lhs), body(thunk(rhs))))
	}

fun Context.compileAs(program: Program): Context? =
	program.matchInfix(asName) { lhs, rhs ->
		push(Rule(Pattern(rhs), body(thunk(lhs))))
	}
