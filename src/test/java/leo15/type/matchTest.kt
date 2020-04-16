package leo15.type

import leo.base.assertEqualTo
import kotlin.test.Test

class MatchTest {
	@Test
	fun matchInfixStaticType() {
		typed(
			"zero" lineTo emptyTyped,
			"plus" lineTo typed("one"))
			.run {
				matchInfix("plus") { lhs, rhs ->
					lhs.plus("minus" lineTo rhs)
				}
			}
			.assertEqualTo(
				typed(
					"zero" lineTo emptyTyped,
					"minus" lineTo typed("one")))
	}

	@Test
	fun matchInfixConstant() {
		typed(
			10.typedLine,
			"plus" lineTo 20.typed)
			.run {
				matchInfix("plus") { lhs, rhs ->
					lhs.plus("minus" lineTo rhs)
				}
			}!!
			.assertEqualTo(
				typed(
					10.typedLine,
					"minus" lineTo 20.typed))
	}

	@Test
	fun matchInfixDynamic() {
		typed(
			10.typedLine,
			"plus" lineTo 20.typed)
			.asDynamic
			.run {
				matchInfix("plus") { lhs, rhs ->
					lhs.plus("minus" lineTo rhs)
				}
			}!!
			.eval
			.assertEqualTo(
				typed(
					10.typedLine,
					"minus" lineTo 20.typed))
	}
}
