package leo14.lambda.value.eval

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.lambda.value.DoubleMinusDoubleValue
import leo14.lambda.value.DoublePlusDoubleValue
import leo14.lambda.value.DoubleTimesDoubleValue
import leo14.lambda.value.StringPlusStringValue
import leo14.lambda.value.value
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun values() {
		term(value(10))
			.evaluate
			.assertEqualTo(evaluated(value(10)))

		term(value("Hello, world!"))
			.evaluate
			.assertEqualTo(evaluated(value("Hello, world!")))

		term(DoublePlusDoubleValue)
			.invoke(term(value(2)))
			.invoke(term(value(3)))
			.evaluate
			.assertEqualTo(evaluated(value(5)))

		term(DoubleMinusDoubleValue)
			.invoke(term(value(5)))
			.invoke(term(value(3)))
			.evaluate
			.assertEqualTo(evaluated(value(2)))

		term(DoubleTimesDoubleValue)
			.invoke(term(value(2)))
			.invoke(term(value(3)))
			.evaluate
			.assertEqualTo(evaluated(value(6)))

		term(StringPlusStringValue)
			.invoke(term(value("Hello, ")))
			.invoke(term(value("world!")))
			.evaluate
			.assertEqualTo(evaluated(value("Hello, world!")))
	}
}