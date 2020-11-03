package leo21.prim.scheme

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.map
import leo14.lambda.scheme.code
import leo14.lambda.term
import leo16.term.chez.eval
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
			.map(Prim::code)
			.code
			.string
			.eval
			.assertEqualTo("5.0")
	}
}