package leo21.prim.js

import leo.base.assertEqualTo
import leo14.lambda.js.code
import leo14.lambda.map
import leo14.lambda.nativeTerm
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.Prim
import kotlin.test.Test

class CodeTest {
	@Test
	fun twoPlusTwo() {
		nativeTerm(NumberMinusNumberPrim)
			.map(Prim::expr)
			.code
			.assertEqualTo("x=>((x)(a=>b=>a))-((x)(a=>b=>b))")
	}

	@Test
	fun sinus() {
		nativeTerm(NumberSinusPrim)
			.map(Prim::expr)
			.code
			.assertEqualTo("x=>(Math.sin)(x)")
	}
}