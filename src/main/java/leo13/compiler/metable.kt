package leo13.compiler

import leo13.Scriptable
import leo13.lineTo
import leo13.script
import leo13.script.TypedLine

data class Metable(
	val isMeta: Boolean,
	val compiled: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "metable"
	override val scriptableBody
		get() = script(
			compiled.scriptableLine,
		"meta" lineTo script(isMeta.toString()))
}

fun metable(isMeta: Boolean, compiled: Compiled) = Metable(isMeta, compiled)
fun metable() = metable(false, compiled())
fun Metable.setMeta(isMeta: Boolean) = metable(isMeta, compiled)

fun Metable.push(typedLine: TypedLine): Compiled? =
	if (isMeta) compiled.append(typedLine)
	else compiled.push(typedLine)
