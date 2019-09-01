package leo13.token.reader

import leo.base.fold
import leo.base.orIfNull
import leo13.Scriptable
import leo13.Space
import leo13.Tab
import leo13.script.Script
import leo13.script.plus
import leo13.script.script

data class TabIndent(val lhsOrNull: TabIndent?, val spaceIndent: SpaceIndent) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "indent"
	override val scriptableBody: Script
		get() =
			lhsOrNull?.scriptableBody.orIfNull { script() }.plus(spaceIndent.scriptableLine)
}

fun indent(spaceIndent: SpaceIndent, vararg spaceIndents: SpaceIndent): TabIndent =
	TabIndent(null, spaceIndent).fold(spaceIndents) { plus(it) }

fun TabIndent.plus(space: Space): TabIndent =
	TabIndent(lhsOrNull, spaceIndent.plus(space))

fun TabIndent?.orNullPlus(space: Space): TabIndent =
	if (this == null) indent(indent(space))
	else plus(space)

fun TabIndent.plus(spaceIndent: SpaceIndent): TabIndent =
	TabIndent(this, spaceIndent)

fun TabIndent?.orNullPlus(spaceIndent: SpaceIndent): TabIndent =
	if (this == null) indent(spaceIndent)
	else plus(spaceIndent)

fun TabIndent.removeOrNull(space: Space): TabIndent? =
	spaceIndent
		.removeOrNull(space)
		?.let { newSpaceIndent -> TabIndent(lhsOrNull, newSpaceIndent) }
		?: lhsOrNull

fun TabIndent.removeOrNull(tab: Tab): TabIndent? =
	lhsOrNull

val TabIndent.reverse: TabIndent
	get() =
		if (lhsOrNull != null) lhsOrNull.reverseInto(TabIndent(null, spaceIndent))
		else this

fun TabIndent.reverseInto(tabIndent: TabIndent): TabIndent =
	TabIndent(tabIndent, spaceIndent).let { newTabIndent ->
		if (lhsOrNull != null) lhsOrNull.reverseInto(newTabIndent)
		else newTabIndent
	}
