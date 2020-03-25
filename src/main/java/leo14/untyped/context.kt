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

fun Context.withGiven(thunk: Thunk): Context =
	push(thunk.givenRule)

fun Context.apply(thunk: Thunk): Thunk? =
	null
		?: applyRules(thunk)
		?: thunk.resolve

fun Context.applyRules(thunk: Thunk): Thunk? =
	when (this) {
		is EmptyContext -> null
		is LinkContext -> link.applyRules(thunk)
	}

fun ContextLink.applyRules(thunk: Thunk): Thunk? =
	definition.apply(context, thunk) ?: context.applyRules(thunk)

fun Context.compile(thunk: Thunk): Context? =
	null
		?: compileDoes(thunk)
		?: compileGives(thunk)
		?: compileAs(thunk)
//?: compileWrites(thunk)

fun Context.compileDoes(thunk: Thunk): Context? =
	thunk.matchInfix(doesName) { lhs, rhs ->
		push(rule(pattern(lhs), evalBody(rhs.script)))
	}

fun Context.compileGives(thunk: Thunk): Context? =
	thunk.matchInfix(givesName) { lhs, rhs ->
		push(rule(pattern(lhs), body(rhs)))
	}

fun Context.compileAs(thunk: Thunk): Context? =
	thunk.matchInfix(asName) { lhs, rhs ->
		push(rule(pattern(rhs), body(lhs)))
	}

//fun Context.compileWrites(thunk: Thunk): Context? =
//	thunk.matchInfix(writesName) { lhs, rhs ->
//		push(rule(pattern(lhs), recurseBody(rhs.script)))
//	}