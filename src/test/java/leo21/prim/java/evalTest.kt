package leo21.prim.java

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.java.eval
import leo14.lambda.map
import leo14.lambda.term
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.prim
import kotlin.test.Test

class EvalTest {
	@Test
	fun doublePlus() {
		term<Prim>(StringPlusStringPrim)
			.invoke(term(prim("Hello, ")))
			.invoke(term(prim("world!")))
			.map(Prim::native)
			.eval
			.assertEqualTo("Hello, world!")
	}
}