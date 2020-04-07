package leo14.untyped.typed

import leo.base.reverseStack
import leo.base.the
import leo13.fold
import leo14.*
import leo14.lambda.runtime.Value

sealed class Scope
object EmptyScope : Scope()
data class LinkScope(val link: ScopeLink) : Scope()
data class ScopeLink(val lhs: Scope, val definition: Definition)

val emptyScope: Scope = EmptyScope
val ScopeLink.scope: Scope get() = LinkScope(this)
infix fun Scope.linkTo(definition: Definition) = ScopeLink(this, definition)
fun Scope.plus(definition: Definition): Scope = linkTo(definition).scope

tailrec fun Scope.apply(typed: Typed): Typed? =
	when (this) {
		is EmptyScope -> null
		is LinkScope -> link.definition.apply(typed) ?: link.lhs.apply(typed)
	}

fun Scope.applyValue(value: Value): Value? =
	apply(value.valueSelfTyped)?.value?.the

fun Scope.bindValue(value: Value): Scope =
	TODO()


fun Scope.apply(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
	if (rhs.isEmpty) applyNormalized(rhs, begin, lhs)
	else applyNormalized(lhs, begin, rhs)

fun Scope.applyNormalized(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
	applyRules(lhs, begin, rhs) ?: lhs.apply(begin, rhs)

fun Scope.applyRules(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled? =
	null

fun Scope.compiled(script: Script): Compiled =
	emptyCompiled.fold(script.lineSeq.reverseStack) { line ->
		compiled(this, line)
	}

fun Scope.compiled(lhs: Compiled, line: ScriptLine): Compiled =
	when (line) {
		is LiteralScriptLine -> lhs.apply(line.literal)
		is FieldScriptLine -> apply(lhs, begin(line.field.string), compiled(line.field.rhs))
	}

