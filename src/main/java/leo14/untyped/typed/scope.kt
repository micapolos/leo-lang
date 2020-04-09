package leo14.untyped.typed

import leo.base.*
import leo13.fold
import leo14.*
import leo14.Begin

sealed class Scope
object EmptyScope : Scope()
data class LinkScope(val link: ScopeLink) : Scope()
data class ScopeLink(val lhs: Scope, val definition: Definition)

val emptyScope: Scope = EmptyScope
val ScopeLink.scope: Scope get() = LinkScope(this)
infix fun Scope.linkTo(definition: Definition) = ScopeLink(this, definition)
fun Scope.plus(definition: Definition): Scope = linkTo(definition).scope

tailrec fun Scope.apply(typed: Compiled): Compiled? =
	when (this) {
		is EmptyScope -> null
		is LinkScope -> link.definition.apply(typed) ?: link.lhs.apply(typed)
	}

fun Scope.apply(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
	if (rhs.isEmpty) applyNormalized(rhs, begin, lhs)
	else applyNormalized(lhs, begin, rhs)

fun Scope.applyNormalized(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
	applyRules(lhs, begin, rhs) ?: lhs.apply(begin, rhs)

fun Scope.applyRules(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled? =
	null

fun Scope.applyDefinition(lhs: Compiled, begin: Begin, rhs: Compiled): Scope? =
	TODO()

fun Scope.compiled(script: Script): Compiled =
	emptyCompiled.fold(script.lineSeq.reverseStack) { line ->
		compiled(this, line)
	}

fun Scope.compiled(lhs: Compiled, line: ScriptLine): Compiled =
	when (line) {
		is LiteralScriptLine -> lhs.apply(line.literal)
		is FieldScriptLine -> apply(lhs, begin(line.field.string), compiled(line.field.rhs))
	}

val Scope.definitionSeq: Seq<Definition>
	get() =
		when (this) {
			EmptyScope -> emptySeq()
			is LinkScope -> seq { link.definition.then(link.lhs.definitionSeq) }
		}
