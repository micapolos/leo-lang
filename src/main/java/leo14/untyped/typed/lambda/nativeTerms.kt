package leo14.untyped.typed.lambda

import leo14.lambda2.Term
import leo14.lambda2.valueApply
import leo14.untyped.typed.stringClass

val Term.nativeStringClassTerm
	get() =
		valueApply { stringClass }

fun Term.nativeClassField(rhs: Term): Term =
	valueApply(rhs) { rhs ->
		(this as Class<*>).getField(rhs as String)
	}