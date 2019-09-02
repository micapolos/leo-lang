package leo13.token.reader

import leo.base.fold
import leo.base.orIfNull
import leo13.LeoObject
import leo13.Space
import leo13.script.Script
import leo13.script.plus
import leo13.script.script

data class Indent(val previousOrNull: Indent?, val tab: SpacesTab) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "indent"
	override val scriptableBody: Script
		get() =
			previousOrNull?.scriptableBody.orIfNull { script() }.plus(tab.scriptableLine)
}

fun indent(tab: SpacesTab, vararg tabs: SpacesTab): Indent =
	Indent(null, tab).fold(tabs) { plus(it) }

fun Indent.plus(space: Space): Indent =
	Indent(previousOrNull, tab.plus(space))

fun Indent?.orNullPlus(space: Space): Indent =
	if (this == null) indent(tab(space))
	else plus(space)

fun Indent.plus(tab: SpacesTab): Indent =
	Indent(this, tab)

fun Indent?.orNullPlus(tab: SpacesTab): Indent =
	if (this == null) indent(tab)
	else plus(tab)

fun Indent.removeOrNull(space: Space): Indent? =
	tab.previousOrNull
		?.let { newSpaceIndent -> Indent(previousOrNull, newSpaceIndent) }
		?: previousOrNull

val Indent.reverse: Indent
	get() =
		if (previousOrNull != null) previousOrNull.reverseInto(Indent(null, tab))
		else this

fun Indent.reverseInto(indent: Indent): Indent =
	Indent(indent, tab).let { newTabIndent ->
		if (previousOrNull != null) previousOrNull.reverseInto(newTabIndent)
		else newTabIndent
	}
