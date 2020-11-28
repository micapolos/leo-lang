package leo23.typed.term

import leo.base.assertEqualTo
import leo14.number
import leo23.type.booleanType
import leo23.type.fields
import leo23.type.numberType
import leo23.type.struct
import leo23.typed.of
import kotlin.test.Test

class EvalTest {
	@Test
	fun struct() {
		("point" struct with(
			"x" struct with(compiled(10)),
			"y" struct with(compiled(20))))
			.eval
			.assertEqualTo(
				listOf(10.number, 20.number).of(
					"point" struct fields(
						"x" struct fields(numberType),
						"y" struct fields(numberType))))
	}

	@Test
	fun numberPlus() {
		compiled(10)
			.numberPlus(compiled(20))
			.eval
			.assertEqualTo(30.number.of(numberType))
	}

	@Test
	fun numberEquals() {
		compiled(10)
			.numberEquals(compiled(10))
			.eval
			.assertEqualTo(true.of(booleanType))
	}
}