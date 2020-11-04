package leo21.prim.js

import leo.base.assertEqualTo
import leo14.lambda.js.code
import leo14.lambda.map
import leo14.lambda.nativeTerm
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.Prim
import kotlin.test.Test

class CodeTest {
	@Test
	fun twoPlusTwo() {
		nativeTerm(DoubleMinusDoublePrim)
			.map(Prim::expr)
			.code
			.assertEqualTo("x=>((x)(a=>b=>a))-((x)(a=>b=>b))")
	}
}