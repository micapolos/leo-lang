package leo21.prim.java

import leo.base.assertEqualTo
import leo14.lambda.java.code
import leo14.lambda.map
import leo14.lambda.nativeTerm
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.Prim
import kotlin.test.Test

class CodeTest {
	@Test
	fun twoPlusTwo() {
		nativeTerm(DoublePlusDoublePrim)
			.map(Prim::native)
			.code
			.assertEqualTo("fn(x->(Double)apply(x, fn(a->fn(b->a)))+(Double)apply(x, fn(a->fn(b->b))))")
	}
}