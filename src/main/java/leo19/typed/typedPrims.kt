package leo19.typed

import leo19.term.term
import leo19.type.booleanType

val Boolean.typed: Typed
	get() =
		typed("boolean" fieldTo term.of(booleanType))