package leo15.lambda.runtime

import leo.base.fold
import leo.stak.contentScript
import leo13.atomName
import leo14.*
import leo15.*

typealias ScriptLineFn<T> = T.() -> ScriptLine

val Term<Any?>.anyScript: Script
	get() =
		script { line(toString()) }

val Closure<Any?>.anyScriptLine: ScriptLine
	get() =
		scriptLine { line(toString()) }

fun <T> Term<T>.script(fn: ScriptLineFn<T>): Script =
	emptyScript.plus(atom.scriptLine(fn)).fold(applicationOrNull.termSeq) { plus(applyName(it.script(fn))) }

fun <T> Atom<T>.scriptLine(fn: ScriptLineFn<T>): ScriptLine =
	when (this) {
		is IndexAtom -> getName(line(literal(index)))
		is ValueAtom -> value.fn()
		is LambdaAtom -> lambdaName(body.script(fn))
	}

fun <T> Closure<T>.scriptLine(fn: ScriptLineFn<T>): ScriptLine =
	closureName(
		scopeName(scope.contentScript { atomName(scriptLine(fn)) }),
		atomName(atom.scriptLine(fn)))