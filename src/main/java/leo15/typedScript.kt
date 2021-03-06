@file:Suppress("UNCHECKED_CAST")

package leo15

import leo14.*
import leo14.untyped.typed.valueJavaScriptLine
import leo14.untyped.typed.valueLiteralOrNull
import leo15.lambda.*

typealias ScriptFn = (Typed) -> Script?

val Typed.script: Script
	get() =
		script(emptyScope)

fun Typed.script(scope: Scope): Script =
	// TODO: Use "script" function from scope
	script { null }

fun Typed.script(scriptFn: ScriptFn): Script =
	scriptFn(this) ?: type.script(term, scriptFn)

fun Type.script(term: Term, scriptFn: ScriptFn): Script =
	when (this) {
		EmptyType -> script()
		AnythingType -> script(anythingName lineTo term.script)
		NothingType -> null!!
		is LinkType -> link.script(term, scriptFn)
		is AlternativeType -> alternative.script(term, scriptFn)
		is FunctionType -> function.script(term, scriptFn)
		is RepeatingType -> repeating.script(term, scriptFn)
		is RecursiveType -> recursive.script(term, scriptFn)
		RecurseType -> recurseScript(term, scriptFn)
	}

fun TypeLink.script(term: Term, scriptFn: ScriptFn): Script =
	if (lhs.isEmpty) script(line.scriptLine(term, scriptFn))
	else term.unsafeUnpair.let { (lhsTerm, rhsTerm) ->
		lhs.typed(lhsTerm).script(scriptFn).plus(line.scriptLine(rhsTerm, scriptFn))
	}

fun TypeLine.scriptLine(term: Term, scriptFn: ScriptFn): ScriptLine =
	when (this) {
		textTypeLine -> line(term.value.valueLiteralOrNull!!)
		numberTypeLine -> line(term.value.valueLiteralOrNull!!)
		is LiteralTypeLine -> line(literal)
		is FieldTypeLine -> field.scriptLine(term, scriptFn)
		JavaTypeLine -> javaScriptLine(term)
	}

fun TypeField.scriptLine(term: Term, scriptFn: ScriptFn): ScriptLine =
	name lineTo rhs.typed(term).script(scriptFn)

fun javaScriptLine(term: Term): ScriptLine =
	term.value.valueJavaScriptLine

fun TypeAlternative.script(term: Term, scriptFn: ScriptFn): Script =
	(term.value as IndexedValue<Term>).let { (index, term) ->
		script(index, term, scriptFn)
	}

fun TypeAlternative.script(index: Int, term: Term, scriptFn: ScriptFn): Script =
	if (index == 0) rhs.script(term, scriptFn)
	else lhs.script(index.dec(), term, scriptFn)

fun Type.script(index: Int, term: Term, scriptFn: ScriptFn): Script =
	if (index == 0) script(term, scriptFn)
	else (this as AlternativeType).alternative.lhs.script(index.dec(), term, scriptFn)

fun TypeFunction.script(term: Term, scriptFn: ScriptFn): Script =
	TODO()

fun TypeRepeating.script(term: Term, scriptFn: ScriptFn): Script =
	if (term == nilTerm) script()
	else term.unsafeUnpair.let { (first, second) ->
		script(first, scriptFn).plus(line.scriptLine(second, scriptFn))
	}

fun TypeRecursive.script(term: Term, scriptFn: ScriptFn): Script =
	TODO()

// TODO: We need to pass TypeRecursive argument for recursion.
fun recurseScript(term: Term, scriptFn: ScriptFn): Script =
	TODO()
