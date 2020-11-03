package leo21.prim.java

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.java.eval
import leo14.lambda.map
import leo14.lambda.term
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.Prim
import leo21.prim.prim
import kotlin.test.Test

class EvalTest {
	@Test
	fun doublePlus() {
		term<Prim>(DoublePlusDoublePrim)
			.invoke(term(prim(2)))
			.invoke(term(prim(3)))
			.map(Prim::native)
			.eval
			.assertEqualTo("5.0")
	}
}