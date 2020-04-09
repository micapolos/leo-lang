package leo14.untyped.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun doApply1() {
		expression(10)
			.doApply { asInt.inc() }
			.assertEqualTo(expression(11))

		expression { 10 }.assertEvaluatesOnce
			.doApply { asInt.inc() }
			.value
			.assertEqualTo(11)
	}

	@Test
	fun doApply2() {
		expression(10)
			.doApply(expression(20)) { asInt + it.asInt }
			.assertEqualTo(expression(30))

		expression(10)
			.doApply(expression { 20 }.assertEvaluatesOnce) { asInt + it.asInt }
			.value
			.assertEqualTo(30)

		expression { 10 }.assertEvaluatesOnce
			.doApply(expression(20)) { asInt + it.asInt }
			.value
			.assertEqualTo(30)

		expression { 10 }.assertEvaluatesOnce
			.doApply(expression { 20 }.assertEvaluatesOnce) { asInt + it.asInt }
			.value
			.assertEqualTo(30)
	}

	@Test
	fun arrays() {
		expression(null)
			.array
			.doApply { asArray.toList() }
			.assertEqualTo(expression(listOf<Value>()))

		expression(null to "foo" to "bar")
			.array
			.doApply { asArray.toList() }
			.assertEqualTo(expression(listOf("foo", "bar")))
	}
}