package leo15

import leo.base.fold
import leo.base.reverse
import leo.stak.*
import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script

data class Scope(val bindingStak: Stak<Binding>) {
	override fun toString() = reflectScriptLine.string
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
val Scope.pop: Scope? get() = bindingStak.pop?.scope

val Scope.pairOrNull: Pair<Scope, Binding>?
	get() =
		bindingStak.unlink?.let { stakLink ->
			stakLink.first.scope to stakLink.second
		}

fun Scope.apply(typed: Typed): Typed? =
	bindingStak.indexedTop { binding ->
		typed.castTo(binding.key.type)?.let { castTyped ->
			binding.value.invoke(this, castTyped.term)
		}
	}
