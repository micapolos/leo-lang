package leo16.lambda.typed

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class TypedOpsTest {
	@Test
	fun getOrNull() {
		typed(
			_point(
				_x(_zero(typed())),
				_y(_one(typed()))))
			.run {
				getOrNull(_x).assertEqualTo(typed(_x(_zero(typed()))))
				getOrNull(_y).assertEqualTo(typed(_y(_one(typed()))))
				getOrNull(_z).assertEqualTo(null)
			}

		typed(
			_point(
				_x(10.typedField),
				_y(20.typedField)))
			.run {
				getOrNull(_x)!!.evaluate.assertEqualTo(typed(_x(10.typedField)))
				getOrNull(_y)!!.evaluate.assertEqualTo(typed(_y(20.typedField)))
				getOrNull(_z).assertEqualTo(null)
			}
	}

	@Test
	fun contentOrNull() {
		typed(
			_point(
				_x(10.typedField),
				_y(20.typedField)))
			.contentOrNull!!
			.assertEqualTo(
				typed(
					_x(10.typedField),
					_y(20.typedField)))
	}
}