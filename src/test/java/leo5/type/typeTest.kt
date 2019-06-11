package leo5.type

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.indexed
import leo5.apply
import leo5.lineTo
import leo5.value
import leo5.valueLine
import kotlin.test.Test
import kotlin.test.assertFails

class TypeTest {
	@Test
	fun compile() {
		type(empty).compile(value()).assertEqualTo(empty)
		assertFails { type(empty).compile(value("foo" lineTo value())) }

		type(int).compile(value(123)).assertEqualTo(123)
		assertFails { type(int).compile(value("int" lineTo value("123123123123" lineTo value()))) }

		type(array(cell(type(int)), 0)).compile(value()).assertEqualTo(listOf<Int>())
		type(array(cell(type(int)), 1)).compile(value(valueLine(10))).assertEqualTo(listOf(10))
		type(array(cell(type(int)), 2)).compile(value(valueLine(10), valueLine(20))).assertEqualTo(listOf(10, 20))
		assertFails { type(array(cell(type(int)), 2)).compile(value(valueLine(10))) }
		assertFails { type(array(cell(type(int)), 2)).compile(value(valueLine(10), valueLine(20), valueLine(30))) }

		type(field("age", type(int))).compile(value("age" lineTo value(42))).assertEqualTo(42)
		assertFails { type(field("age", type(int))).compile(value(42)) }

		type(application(type(int), field("plus", type(empty))))
			.compile(value(123).apply("plus" lineTo value()))
			.assertEqualTo(Pair(123, empty))

		type(struct()).compile(value()).assertEqualTo(listOf<Any>())
		assertFails { type(struct()).compile(value(123)) }

		type(struct(field("x", type(int)), field("y", type(int))))
			.compile(value("x" lineTo value(1), "y" lineTo value(2)))
			.assertEqualTo(listOf(1, 2))

		type(oneOf(type(empty), type(int))).compile(value()).assertEqualTo(0 indexed empty)
		type(oneOf(type(empty), type(int))).compile(value(123)).assertEqualTo(1 indexed 123)
		assertFails { type(oneOf(type(empty), type(int))).compile(value("x" lineTo value())) }
	}
}