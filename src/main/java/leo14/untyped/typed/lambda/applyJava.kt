package leo14.untyped.typed.lambda

import leo14.lambda2.valueTerm

val Typed.javaApply: Typed?
	get() =
		null
			?: applyJavaClassName
			?: applyJavaClassNameText
			?: applyJavaClassField

val Typed.applyJavaClassName: Typed?
	get() =
		matchJavaClassName { valueTerm }

val Typed.applyJavaClassNameText: Typed?
	get() =
		matchJavaClassNameText { javaStringClassTerm }

val Typed.applyJavaClassField: Typed?
	get() =
		matchJavaClassField { javaClassField(it) }
