package leo13.compiler

import leo13.AsScriptLine
import leo13.lineTo
import leo13.script
import leo13.script.TypedLine

data class Metable(
	val isMeta: Boolean,
	val compiled: Compiled) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "metable" lineTo script(
		compiled.asScriptLine,
		"meta" lineTo script(isMeta.toString()))
}

fun metable(isMeta: Boolean, compiled: Compiled) = Metable(isMeta, compiled)
fun metable() = metable(false, compiled())
fun Metable.setMeta(isMeta: Boolean) = metable(isMeta, compiled)

fun Metable.push(typedLine: TypedLine): Compiled? =
	if (isMeta) compiled.append(typedLine)
	else compiled.push(typedLine)
