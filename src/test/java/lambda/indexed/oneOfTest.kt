package lambda.indexed

import leo.base.assertEqualTo
import leo.base.nat
import kotlin.test.Test

class OneOfTest {
	@Test
	fun testOneOf() {
		oneOf(1, 3)
			.invoke(id, constant(10.nat.term), constant(20.nat.term), constant(30.nat.term))
			.eval
			.assertEqualTo(10.nat.term)
		oneOf(2, 3)
			.invoke(id, constant(10.nat.term), constant(20.nat.term), constant(30.nat.term))
			.eval
			.assertEqualTo(20.nat.term)
		oneOf(3, 3)
			.invoke(id, constant(10.nat.term), constant(20.nat.term), constant(30.nat.term))
			.eval
			.assertEqualTo(30.nat.term)

		oneOf(1, 3)
			.invoke(10.nat.term, id, id, id)
			.eval
			.assertEqualTo(10.nat.term)
		oneOf(2, 3)
			.invoke(10.nat.term, id, id, id)
			.eval
			.assertEqualTo(10.nat.term)
		oneOf(3, 3)
			.invoke(10.nat.term, id, id, id)
			.eval
			.assertEqualTo(10.nat.term)
	}
}