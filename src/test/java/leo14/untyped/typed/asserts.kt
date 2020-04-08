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

val <T> Dynamic<T>.assertEvaluatedOnce: Dynamic<T>
	get() {
		var evaluated = false
		return dynamic {
			evaluated.assertEqualTo(false, "Evaluated twice")
			evaluated = true
			value
		}
	}
