package leo21.scoped

import leo21.typed.Typed

data class Scoped(
	val scope: Scope,
	val body: Typed
)

