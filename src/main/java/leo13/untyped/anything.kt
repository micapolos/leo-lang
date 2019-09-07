package leo13.untyped

import leo13.scripter.toString

object Anything {
	override fun toString() = anythingType.toString(this)
}

val anything = Anything

val anythingType = leo13.scripter.scripter("anything", anything)
