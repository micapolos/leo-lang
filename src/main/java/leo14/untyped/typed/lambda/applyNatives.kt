package leo14.untyped.typed.lambda

import leo14.lambda2.value

val Typed.nativeApply: Typed?
	get() =
		null
			?: applyNativeClassName
			?: applyNativeClassNameText
			?: applyNativeClassField

val Typed.applyNativeClassName: Typed?
	get() =
		matchNativeClassName { value(this) }

val Typed.applyNativeClassNameText: Typed?
	get() =
		matchNativeClassNameText { nativeStringClassTerm }

val Typed.applyNativeClassField: Typed?
	get() =
		matchNativeClassField { nativeClassField(it) }
