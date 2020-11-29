package leo23.typed.term

import leo.base.assertEqualTo
import leo.base.assertNull
import leo23.term.expr
import leo23.term.tuple
import leo23.term.tupleAt
import leo23.type.fields
import leo23.type.numberType
import leo23.type.struct
import leo23.typed.of
import kotlin.test.Test

class UtilTest {
	@Test
	fun make() {
		compiled(
			"x" struct with(compiled(10)),
			"y" struct with(compiled(20)))
			.make("point")
			.assertEqualTo(
				"point" struct with(
					"x" struct with(compiled(10)),
					"y" struct with(compiled(20))))
	}

	@Test
	fun getOrNull_0() {
		("point" struct with(
			"x" struct with(compiled(10)),
			"y" struct with(compiled(20))))
			.getOrNull("x")
			.assertEqualTo(
				tuple(expr(10), expr(20))
					.tupleAt(0)
					.of("x" struct fields(numberType)))
	}

	@Test
	fun getOrNull_1() {
		("point" struct with(
			"x" struct with(compiled(10)),
			"y" struct with(compiled(20))))
			.getOrNull("y")
			.assertEqualTo(
				tuple(expr(10), expr(20))
					.tupleAt(1)
					.of("y" struct fields(numberType)))
	}

	@Test
	fun getOrNull_mismatch() {
		("point" struct with(
			"x" struct with(compiled(10)),
			"y" struct with(compiled(20))))
			.getOrNull("z")
			.assertNull
	}
}