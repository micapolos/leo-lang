package leo14.untyped.typed.lambda

import leo14.lambda2.value

val Typed.javaApply: Typed?
	get() =
		null
			?: applyJavaClassName
			?: applyJavaClassNameText
			?: applyJavaClassField

val Typed.applyJavaClassName: Typed?
	get() =
		matchJavaClassName { value(this) }

val Typed.applyJavaClassNameText: Typed?
	get() =
		matchJavaClassNameText { javaStringClassTerm }

val Typed.applyJavaClassField: Typed?
	get() =
		matchJavaClassField { javaClassField(it) }
