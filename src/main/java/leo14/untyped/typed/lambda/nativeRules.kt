package leo14.untyped.typed.lambda

import leo14.lambda2.value

val Compiled.applyNativeClassName: Compiled?
	get() =
		matchNativeClassName { value(this) }

val Compiled.applyNativeClassNameText: Compiled?
	get() =
		matchNativeClassNameText { nativeStringClassTerm }
