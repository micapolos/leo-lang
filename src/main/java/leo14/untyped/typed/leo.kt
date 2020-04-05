package leo14.untyped.typed

import leo14.Begin

data class Leo(
	val parentOrNull: LeoParent?,
	val resolver: Resolver)

data class LeoParent(
	val leo: Leo,
	val begin: Begin,
	val endFn: (Leo) -> Leo)
