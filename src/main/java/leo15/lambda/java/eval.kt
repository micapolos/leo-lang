package leo15.lambda.java

import leo15.lambda.runtime.*
import leo15.lambda.Term as LambdaTerm

val LambdaTerm.eval: Thunk<Any?>
	get() =
		term<Any?>(this).anyEval

val Term<Java>.evalThunk: Thunk<Java>
	get() =
		eval(Java::apply)

val Term<Java>.evalJava: Java
	get() =
		(evalThunk.atom as ValueAtom<Java>).value
