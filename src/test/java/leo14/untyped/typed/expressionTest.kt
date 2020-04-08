package leo14.untyped.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun doApply1() {
		constant(10).expression
			.doApply { inc() }
			.assertEqualTo(constant(11).expression)

		dynamic { 10 }.assertEvaluatesOnce.expression
			.doApply { inc() }
			.value
			.assertEqualTo(11)
	}

	@Test
	fun doApply2() {
		constant(10).expression
			.doApply(constant(20).expression) { this + it }
			.assertEqualTo(constant(30).expression)

		constant(10).expression
			.doApply(dynamic { 20 }.assertEvaluatesOnce.expression) { this + it }
			.value
			.assertEqualTo(30)

		dynamic { 10 }.assertEvaluatesOnce.expression
			.doApply(constant(20).expression) { this + it }
			.value
			.assertEqualTo(30)

		dynamic { 10 }.assertEvaluatesOnce.expression
			.doApply(dynamic { 20 }.assertEvaluatesOnce.expression) { this + it }
			.value
			.assertEqualTo(30)
	}
}