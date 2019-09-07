package leo13.base

import leo13.base.type.Type
import leo13.base.type.scriptLine

abstract class Typed<V : Any> {
	abstract val type: Type<V>
	override fun toString() = type.scriptLine(this).toString()
}