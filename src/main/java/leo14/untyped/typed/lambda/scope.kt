package leo14.untyped.typed.lambda

import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.topIndexedValue
import java.lang.reflect.Type

data class Scope(val entryStak: Stak<Entry>)

val Stak<Entry>.scope get() = Scope(this)
val emptyScope = emptyStak<Entry>().scope

fun Scope.indexedEntry(type: Type): IndexedValue<Entry>? =
	entryStak.topIndexedValue { it.type == type }
