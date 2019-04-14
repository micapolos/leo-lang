package leo32.rt

import leo.base.Empty

data class Scope(
	val unit: Unit)

val Empty.scope
	get() =
		Scope(Unit)