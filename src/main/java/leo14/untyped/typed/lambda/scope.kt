package leo14.untyped.typed.lambda

import leo.base.fold
import leo.base.reverse
import leo.stak.*
import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script
import leo14.untyped.leoString
import leo14.untyped.typed.Type

data class Scope(val bindingStak: Stak<Binding>) {
	override fun toString() = reflectScriptLine.leoString
}

val Scope.reflectScriptLine: ScriptLine
	get() =
		"scope" lineTo script(
			"binding" lineTo script(
				"list" lineTo script()
					.fold(bindingStak.seq.reverse) { plus(it.reflectScriptLine) }))

val Stak<Binding>.scope get() = Scope(this)
val emptyScope = emptyStak<Binding>().scope
fun Scope.plus(entry: Binding): Scope = bindingStak.push(entry).scope

fun Scope.indexedBinding(type: Type): IndexedValue<Binding>? =
	bindingStak.topIndexedValue { it.key.type == type }

fun Scope.apply(typed: Typed): Typed? =
	indexedBinding(typed.type)?.let { indexedBinding ->
		indexedBinding.value.value.invoke(indexedBinding.index, typed.term)
	}
