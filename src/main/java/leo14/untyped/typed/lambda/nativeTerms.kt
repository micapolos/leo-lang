package leo14.untyped.typed.lambda

import leo14.lambda2.Term
import leo14.lambda2.valueApply
import leo14.untyped.typed.stringClass

val Term.javaStringClassTerm
	get() =
		valueApply { stringClass }

fun Term.javaClassField(rhs: Term): Term =
	valueApply(rhs) { rhs ->
		(this as Class<*>).getField(rhs as String)
	}