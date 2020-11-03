package leo21.prim.js

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.js.code
import leo14.lambda.map
import leo14.lambda.term
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.Prim
import leo21.prim.prim
import kotlin.test.Test

class CodeTest {
	@Test
	fun twoPlusTwo() {
		term(DoubleMinusDoublePrim)
			.invoke(term(prim(5)))
			.invoke(term(prim(3)))
			.map(Prim::expr)
			.code
			.assertEqualTo("((a=>b=>(a)-(b))(5))(3)")
	}
}