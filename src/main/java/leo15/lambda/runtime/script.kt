package leo15.lambda.runtime

import leo.base.ifNotNull
import leo.stak.contentScript
import leo13.atomName
import leo14.*
import leo15.*

typealias ScriptLineFn<T> = T.() -> ScriptLine

val Term<Any?>.anyScript: Script
	get() =
		script { line(toString()) }

val Thunk<Any?>.anyScriptLine: ScriptLine
	get() =
		scriptLine { line(toString()) }

fun <T> Term<T>.script(fn: ScriptLineFn<T>): Script =
	emptyScript.plus(this, fn)

fun <T> Script.plus(term: Term<T>, fn: ScriptLineFn<T>): Script =
	plus(applyName(term.atom.scriptLine(fn))).ifNotNull(term.applicationOrNull) { plus(it, fn) }

fun <T> Script.plus(application: Application<T>, fn: ScriptLineFn<T>): Script =
	plus(application.term, fn).ifNotNull(application.applicationOrNull) { plus(it, fn) }

fun <T> Atom<T>.scriptLine(fn: ScriptLineFn<T>): ScriptLine =
	when (this) {
		is IndexAtom -> getName(line(literal(index)))
		is ValueAtom -> value.fn()
		is TermAtom -> lambdaName(term.script(fn))
	}

fun <T> Thunk<T>.scriptLine(fn: ScriptLineFn<T>): ScriptLine =
	thunkName(
		scopeName(scope.contentScript { atomName(scriptLine(fn)) }),
		atomName(atom.scriptLine(fn)))