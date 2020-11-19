package leo21.prim.julia

import leo.base.assertEqualTo
import leo14.lambda.julia.code
import leo14.lambda.map
import leo14.lambda.term
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.Prim
import kotlin.test.Test

class CodeTest {
	@Test
	fun twoPlusTwo() {
		term<Prim>(NumberPlusNumberPrim)
			.map(Prim::julia)
			.code
			.assertEqualTo("x->x(a->b->a)+x(a->b->b)")
	}
}
