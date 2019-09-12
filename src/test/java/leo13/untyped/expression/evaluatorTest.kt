package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.untyped.value.value
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun evaluate() {
		evaluator()
			.plus(constant(value("foo")))
			.assertEqualTo(evaluator().set(value("foo")))
	}
}