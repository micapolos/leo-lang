package leo14.lambda.value.eval

import leo.base.assertEqualTo
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.lambda.value.DoubleMinusValue
import leo14.lambda.value.DoublePlusValue
import leo14.lambda.value.DoubleTimesValue
import leo14.lambda.value.StringPlusValue
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

		fn(fn(term(DoublePlusValue)))
			.invoke(term(value(2)))
			.invoke(term(value(3)))
			.evaluate
			.assertEqualTo(evaluated(value(5)))

		fn(fn(term(DoubleMinusValue)))
			.invoke(term(value(5)))
			.invoke(term(value(3)))
			.evaluate
			.assertEqualTo(evaluated(value(2)))

		fn(fn(term(DoubleTimesValue)))
			.invoke(term(value(2)))
			.invoke(term(value(3)))
			.evaluate
			.assertEqualTo(evaluated(value(6)))

		fn(fn(term(StringPlusValue)))
			.invoke(term(value("Hello, ")))
			.invoke(term(value("world!")))
			.evaluate
			.assertEqualTo(evaluated(value("Hello, world!")))
	}
}