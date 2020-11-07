package leo21.prim.julia

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.julia.code
import leo14.lambda.map
import leo14.lambda.term
import leo16.term.julia.eval
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoubleSinusPrim
import leo21.prim.Prim
import leo21.prim.prim
import leo21.term.plus
import kotlin.math.sin
import kotlin.test.Test

class EvalTest {
	@Test
	fun doublePlus() {
		term<Prim>(DoubleMinusDoublePrim)
			.invoke(term(prim(5.0)).plus(term(prim(3.0))))
			.map(Prim::julia)
			.code
			.eval
			.assertEqualTo("2.0")
	}

	@Test
	fun doubleSinus() {
		term<Prim>(DoubleSinusPrim)
			.invoke(term(prim(1)))
			.map(Prim::julia)
			.code
			.eval
			.assertEqualTo("${sin(1.0)}")
	}
}