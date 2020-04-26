package leo15.lambda.runtime.java

import leo15.lambda.runtime.Closure
import leo15.lambda.runtime.Term
import leo15.lambda.runtime.ValueAtom
import leo15.lambda.runtime.eval

val Term<Java>.evalClosure: Closure<Java>
	get() =
		eval(Java::apply)

val Term<Java>.evalJava: Java
	get() =
		(evalClosure.atom as ValueAtom<Java>).value
