package leo21.prim.julia

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.julia.code
import leo14.lambda.map
import leo14.lambda.term
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.Prim
import leo21.prim.prim
import kotlin.test.Test

class CodeTest {
	@Test
	fun twoPlusTwo() {
		term<Prim>(DoublePlusDoublePrim)
			.invoke(term(prim(2)))
			.invoke(term(prim(3)))
			.map(Prim::julia)
			.code
			.assertEqualTo("((a->b->a+b)(2.0))(3.0)")
	}
}
