package vm

import leo.base.assertEqualTo
import kotlin.test.Test

class ValueTest {
	@Test
	fun type() {
		value(123).type.assertEqualTo(int)
		value(123.0f).type.assertEqualTo(float)
		value("123").type.assertEqualTo(string)
		value().type.assertEqualTo(struct())
		value("x" of value(10), "y" of value(20)).type.assertEqualTo(struct("x" of int, "y" of int))
		array(value(10), value(20)).type.assertEqualTo(int[2])
	}
}