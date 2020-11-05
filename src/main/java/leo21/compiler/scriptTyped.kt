package leo21.compiler

import leo14.Script
import leo21.compiled.Compiled

val Script.compiled: Compiled
	get() =
		emptyBindings.compiled(this)