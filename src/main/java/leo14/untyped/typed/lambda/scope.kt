package leo14.untyped.typed.lambda

import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.push
import leo.stak.topIndexedValue
import leo14.lambda2.at
import leo14.untyped.typed.Type

data class Scope(val entryStak: Stak<Entry>)

val Stak<Entry>.scope get() = Scope(this)
val emptyScope = emptyStak<Entry>().scope
fun Scope.plus(entry: Entry): Scope = entryStak.push(entry).scope

fun Scope.indexedEntry(type: Type): IndexedValue<Entry>? =
	entryStak.topIndexedValue { it.type == type }

fun Scope.apply(compiled: Compiled): Compiled? =
	indexedEntry(compiled.type)?.let { indexedEntry ->
		indexedEntry.value.compiled.type.compiled(at(indexedEntry.index))
	}