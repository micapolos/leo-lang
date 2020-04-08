package leo14.untyped.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun doApply1() {
		expression(10)
			.doApply { inc() }
			.assertEqualTo(expression(11))

		expression { 10 }.assertEvaluatesOnce
			.doApply { inc() }
			.value
			.assertEqualTo(11)
	}

	@Test
	fun doApply2() {
		expression(10)
			.doApply(expression(20)) { this + it }
			.assertEqualTo(expression(30))

		expression(10)
			.doApply(expression { 20 }.assertEvaluatesOnce) { this + it }
			.value
			.assertEqualTo(30)

		expression { 10 }.assertEvaluatesOnce
			.doApply(expression(20)) { this + it }
			.value
			.assertEqualTo(30)

		expression { 10 }.assertEvaluatesOnce
			.doApply(expression { 20 }.assertEvaluatesOnce) { this + it }
			.value
			.assertEqualTo(30)
	}
}