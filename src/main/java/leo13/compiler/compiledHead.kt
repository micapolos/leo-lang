package leo13.compiler

import leo.base.notNullIf
import leo13.script.Scriptable
import leo13.script.script
import leo13.token.*
import leo13.type.Typed
import leo13.type.lineTo
import leo13.type.typed
import leo9.isEmpty
import leo9.linkOrNull

data class CompiledHead(
	val openers: CompiledOpeners,
	val metable: Metable) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "head"
	override val scriptableBody get() = script(openers.scriptableLine, metable.scriptableLine)
}

fun head(openers: CompiledOpeners, metable: Metable) = CompiledHead(openers, metable)
fun head(typed: Typed) = head(compiledOpeners(), metable(false, compiled(context(), typed)))
val CompiledHead.completedCompiledOrNull: Compiled? get() = notNullIf(openers.stack.isEmpty) { metable.compiled }
val Metable.head get() = head(compiledOpeners(), this)

fun CompiledHead.plus(token: Token): CompiledHead? =
	when (token) {
		is OpeningToken -> plus(token.opening)
		is ClosingToken -> plus(token.closing)
	}

fun CompiledHead.plus(opening: Opening): CompiledHead =
	if (!metable.isMeta && opening.name == "meta") head(openers, metable.setMeta(true))
	else head(
		openers.push(metable openerTo opening),
		metable(false, compiled(metable.compiled.context, typed())))

fun CompiledHead.plus(closing: Closing): CompiledHead? =
	if (metable.isMeta) head(openers, metable.setMeta(false))
	else openers.stack.linkOrNull?.let { openerStackLink ->
		openerStackLink.value.let { opener ->
			opener.lhs.push(opener.opening.name lineTo metable.compiled.typed)?.let { compiled ->
				head(compiledOpeners(openerStackLink.stack), metable(openerStackLink.value.lhs.isMeta, compiled))
			}
		}
	}
