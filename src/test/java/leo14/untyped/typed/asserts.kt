package leo14.untyped.typed

import leo.base.assertEqualTo

val Compiled.erasedOnce: Compiled
	get() {
		var erased = false
		return type.compiled {
			erased.assertEqualTo(false, "Erased twice")
			erased = true
			erase()
		}
	}