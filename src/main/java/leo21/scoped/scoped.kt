package leo21.scoped

import leo21.compiled.Compiled

data class Scoped(
	val scope: Scope,
	val body: Compiled
)
