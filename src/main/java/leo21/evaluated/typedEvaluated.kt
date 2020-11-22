package leo21.evaluated

import leo21.prim.runtime.value
import leo21.compiled.Compiled

val Compiled.evaluated: Evaluated
	get() =
		Evaluated(term.value, type)
