package leo13.script

import leo13.Scriptable
import leo13.lineTo
import leo13.script

data class Metable(val context: Context, val isMeta: Boolean) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine = "metable" lineTo script(
		context.asScriptLine,
		"meta" lineTo script(isMeta.toString() lineTo script()))
}

fun metable(context: Context, isMeta: Boolean) = Metable(context, isMeta)
fun metable() = Metable(context(), false)

fun Metable.setMeta(isMeta: Boolean) = metable(context, isMeta)