package leo15

import leo14.lambda2.Term
import leo14.lambda2.valueApply
import leo14.untyped.typed.stringClass

val Term.javaStringClassTerm: Term
	get() =
		valueApply {
			try {
				stringClass
			} catch (e: Throwable) {
				e
			}
		}

fun Term.javaClassField(rhs: Term): Term =
	valueApply(rhs) { rhs ->
		try {
			(this as Class<*>).getField(rhs as String)
		} catch (e: Throwable) {
			e
		}
	}
