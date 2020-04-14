package leo15

import leo14.untyped.typed.stringClass
import leo15.lambda.Term
import leo15.lambda.valueApply

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
