package leo13.tokenizer

import leo.base.fold
import leo.base.orIfNull
import leo13.LeoObject
import leo13.Space
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script

data class SpacesTab(val previousOrNull: SpacesTab?, val space: Space) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "tab"
	override val scriptableBody: Script
		get() =
			previousOrNull
				?.scriptableBody
				.orIfNull { script() }
				.plus("space" lineTo script())
}

fun tab(space: Space, vararg spaces: Space) = SpacesTab(null, space).fold(spaces) { plus(it) }
fun SpacesTab?.plus(space: Space): SpacesTab = SpacesTab(this, space)
