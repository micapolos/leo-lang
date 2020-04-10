package leo14.untyped.typed.lambda

import leo14.lambda2.Term
import leo14.lambda2.fn
import leo14.lambda2.value

val Term.javaStringClassTerm: Term
	get() =
		fn { value("leo14.MainKt.class.getClassLoader().loadClass(${it.value})") }
