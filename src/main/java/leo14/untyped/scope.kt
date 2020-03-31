package leo14.untyped

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo14.Script

sealed class Scope
data class EmptyScope(val empty: Empty) : Scope()
data class LinkScope(val link: ScopeLink) : Scope()

data class ScopeLink(val scope: Scope, val definition: Definition)

infix fun Scope.linkTo(definition: Definition) = ScopeLink(this, definition)
infix fun Scope.linkTo(rule: Rule) = linkTo(definition(rule))

fun scope() = EmptyScope(empty) as Scope
fun scope(link: ScopeLink): Scope = LinkScope(link)

fun scope(rule: Rule, vararg rules: Rule) =
	scope().push(rule).fold(rules) { push(it) }

fun Scope.push(definition: Definition): Scope =
	scope(this linkTo definition)

fun Scope.push(rule: Rule): Scope =
	scope(this linkTo rule)

fun Scope.withGiven(thunk: Thunk): Scope =
	push(thunk.givenRule)

fun Scope.apply(sequence: Sequence): Resolver =
	sequence.resolveAccess?.let { resolver(it) }
		?: resolve(thunk(value(sequence.normalize)))

fun Scope.apply(thunk: Thunk): Applied? =
	null
		?: applyRules(thunk)
		?: thunk.resolve?.let { applied(it) }

fun Scope.applyRules(thunk: Thunk): Applied? =
	when (this) {
		is EmptyScope -> null
		is LinkScope -> link.applyRules(thunk)
	}

fun ScopeLink.applyRules(thunk: Thunk): Applied? =
	definition.apply(scope, thunk) ?: scope.applyRules(thunk)

fun Scope.compile(thunk: Thunk): Scope? =
	null
		?: compileDoes(thunk)
		?: compileGives(thunk)
		?: compileAs(thunk)

fun Scope.compileDoes(thunk: Thunk): Scope? =
	thunk.value.sequenceOrNull?.matchInfixOrPrefix(givesName) { lhs, rhs ->
		push(rule(pattern(lhs), evalBody(rhs.script)))
	}

fun Scope.compileGives(thunk: Thunk): Scope? =
	thunk.value.sequenceOrNull?.matchInfixOrPrefix(isName) { lhs, rhs ->
		push(rule(pattern(lhs), body(rhs)))
	}

fun Scope.compileAs(thunk: Thunk): Scope? =
	thunk.matchInfix(asName) { lhs, rhs ->
		push(rule(pattern(rhs), body(lhs)))
	}

fun Scope.evaluate(script: Script): Thunk =
	resolver(emptyThunk).evaluate(script).thunk