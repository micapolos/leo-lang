package leo15.lambda.java

import leo15.lambda.runtime.Term
import leo15.lambda.runtime.Thunk
import leo15.lambda.runtime.ValueAtom
import leo15.lambda.runtime.eval

val Term<Java>.evalThunk: Thunk<Java>
	get() =
		eval(Java::apply)

val Term<Java>.eval: Java
	get() =
		(evalThunk.atom as ValueAtom<Java>).value
