package leo15.type

import leo15.plusName

val Typed.apply: Typed
	get() =
		null
			?: applyNumberPlusNumber
			?: applyGet
			?: this

val Typed.applyNumberPlusNumber: Typed?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			TODO()
		}

val Typed.applyGet: Typed?
	get() =
		matchField { name, rhs ->
			rhs.get(name)
		}
