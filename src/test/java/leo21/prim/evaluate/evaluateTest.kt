package leo21.prim.evaluate

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.pair
import leo14.lambda.term
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.prim
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun values() {
		term(prim(10))
			.evaluate
			.assertEqualTo(term(prim(10)))

		term(prim("Hello, world!"))
			.evaluate
			.assertEqualTo(term(prim("Hello, world!")))

		term(DoublePlusDoublePrim)
			.invoke(pair(term(prim(2)), term(prim(3))))
			.evaluate
			.assertEqualTo(term(prim(5)))

		term(DoubleMinusDoublePrim)
			.invoke(pair(term(prim(5)), term(prim(3))))
			.evaluate
			.assertEqualTo(term(prim(2)))

		term(DoubleTimesDoublePrim)
			.invoke(pair(term(prim(2)), term(prim(3))))
			.evaluate
			.assertEqualTo(term(prim(6)))

		term(StringPlusStringPrim)
			.invoke(pair(term(prim("Hello, ")), term(prim("world!"))))
			.evaluate
			.assertEqualTo(term(prim("Hello, world!")))
	}
}