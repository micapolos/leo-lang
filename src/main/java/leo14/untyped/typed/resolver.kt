package leo14.untyped.typed

import leo14.Begin
import leo14.Literal

data class Resolver(
	val typed: Typed,
	val scope: Scope,
	val beginFn: Leo.(Begin) -> Leo,
	val literalFn: Leo.(Literal) -> Leo)
