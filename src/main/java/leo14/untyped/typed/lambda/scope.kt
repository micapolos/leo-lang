package leo14.untyped.typed.lambda

import leo.base.fold
import leo.base.reverse
import leo.stak.*
import leo14.ScriptLine
import leo14.lambda2.at
import leo14.lineTo
import leo14.plus
import leo14.script
import leo14.untyped.leoString
import leo14.untyped.typed.Type

data class Scope(val entryStak: Stak<Entry>) {
	override fun toString() = reflectScriptLine.leoString
}

val Scope.reflectScriptLine: ScriptLine
	get() =
		"scope" lineTo script().fold(entryStak.seq.reverse) { plus(it.reflectScriptLine) }

val Stak<Entry>.scope get() = Scope(this)
val emptyScope = emptyStak<Entry>().scope
fun Scope.plus(entry: Entry): Scope = entryStak.push(entry).scope

fun Scope.indexedEntry(type: Type): IndexedValue<Entry>? =
	entryStak.topIndexedValue { it.type == type }

fun Scope.apply(typed: Typed): Typed? =
	indexedEntry(typed.type)?.let { indexedEntry ->
		indexedEntry.value.typed.type.typed(at(indexedEntry.index))
	}