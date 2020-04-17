package leo15.type

import leo.base.assertEqualTo
import leo14.bigDecimal
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

	@Test
	fun matchPrimitives() {
		10.javaTyped
			.matchJava { it.of(javaType) }
			.assertEqualTo(10.javaTyped)

		10.typed
			.matchNumber { expression -> expression.of(javaType) }
			.assertEqualTo(10.bigDecimal.javaTyped)

		"foo".typed
			.matchText { expression -> expression.of(javaType) }
			.assertEqualTo("foo".javaTyped)

		"foo".typed.asDynamic
			.matchText { expression -> expression.of(javaType) }!!
			.eval
			.assertEqualTo("foo".javaTyped)
	}
}
