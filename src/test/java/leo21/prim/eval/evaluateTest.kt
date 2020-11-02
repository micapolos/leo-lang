package leo21.prim.eval

import leo.base.assertEqualTo
import leo14.lambda.invoke
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
			.invoke(term(prim(2)))
			.invoke(term(prim(3)))
			.evaluate
			.assertEqualTo(term(prim(5)))

		term(DoubleMinusDoublePrim)
			.invoke(term(prim(5)))
			.invoke(term(prim(3)))
			.evaluate
			.assertEqualTo(term(prim(2)))

		term(DoubleTimesDoublePrim)
			.invoke(term(prim(2)))
			.invoke(term(prim(3)))
			.evaluate
			.assertEqualTo(term(prim(6)))

		term(StringPlusStringPrim)
			.invoke(term(prim("Hello, ")))
			.invoke(term(prim("world!")))
			.evaluate
			.assertEqualTo(term(prim("Hello, world!")))
	}
}