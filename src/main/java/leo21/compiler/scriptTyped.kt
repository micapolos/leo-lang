package leo21.compiler

import leo14.Script
import leo21.typed.Typed

val Script.typed: Typed
	get() =
		emptyBindings.typed(this)