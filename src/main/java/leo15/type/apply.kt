package leo15.type

import leo15.plusName

val Typed.applyNumberPlusNumber: Typed?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			TODO()
		}