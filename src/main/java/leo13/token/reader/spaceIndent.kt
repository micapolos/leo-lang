package leo13.token.reader

import leo.base.fold
import leo.base.orIfNull
import leo13.Space
import leo13.script.*

data class SpaceIndent(val lhsOrNull: SpaceIndent?, val space: Space) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "indent"
	override val scriptableBody: Script
		get() =
			lhsOrNull
				?.scriptableBody
				.orIfNull { script() }
				.plus("space" lineTo script())
}

fun indent(space: Space, vararg spaces: Space) = SpaceIndent(null, space).fold(spaces) { plus(it) }
fun SpaceIndent?.plus(space: Space): SpaceIndent = SpaceIndent(this, space)
fun SpaceIndent.removeOrNull(space: Space): SpaceIndent? = lhsOrNull