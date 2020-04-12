package leo14.untyped.typed.lambda

import leo14.*
import leo14.lambda2.Term
import leo14.lambda2.script
import leo14.lambda2.unsafeUnpair
import leo14.lambda2.value
import leo14.untyped.anythingName
import leo14.untyped.typed.*

typealias ScriptFn = (Typed) -> Script?

fun Typed.script(scope: Scope): Script =
	// TODO: Use "script" function from scope
	script { null }

fun Typed.script(scriptFn: ScriptFn): Script =
	scriptFn(this) ?: when (type) {
		EmptyType -> script()
		AnythingType -> script(anythingName lineTo term.script)
		NothingType -> null!!
		is LinkType -> type.link.script(term, scriptFn)
		is AlternativeType -> type.alternative.script(term, scriptFn)
		is FunctionType -> type.function.script(term, scriptFn)
		is RepeatingType -> type.repeating.script(term, scriptFn)
		is RecursiveType -> type.recursive.script(term, scriptFn)
		RecurseType -> recurseScript(term, scriptFn)
	}

fun TypeLink.script(term: Term, scriptFn: ScriptFn): Script =
	term.unsafeUnpair.let { (lhsTerm, rhsTerm) ->
		lhs.typed(lhsTerm).script(scriptFn).plus(line.scriptLine(rhsTerm, scriptFn))
	}

fun TypeLine.scriptLine(term: Term, scriptFn: ScriptFn): ScriptLine =
	when (this) {
		is LiteralTypeLine -> TODO() // remove this type line
		is FieldTypeLine -> field.scriptLine(term, scriptFn)
		NativeTypeLine -> nativeScriptLine(term)
	}

fun TypeField.scriptLine(term: Term, scriptFn: ScriptFn): ScriptLine =
	name lineTo rhs.typed(term).script(scriptFn)

fun nativeScriptLine(term: Term): ScriptLine =
	term.value.nativeScriptLine

fun TypeAlternative.script(term: Term, scriptFn: ScriptFn): Script =
	TODO()

fun TypeFunction.script(term: Term, scriptFn: ScriptFn): Script =
	TODO()

fun TypeRepeating.script(term: Term, scriptFn: ScriptFn): Script =
	TODO()

fun TypeRecursive.script(term: Term, scriptFn: ScriptFn): Script =
	TODO()

// TODO: We need to pass TypeRecursive argument for recursion.
fun recurseScript(term: Term, scriptFn: ScriptFn): Script =
	TODO()
