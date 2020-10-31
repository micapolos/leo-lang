package leo14.lambda.value

import leo.base.assertEqualTo
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.term
import kotlin.test.Test

class EvalTest {
	@Test
	fun values() {
		term(value(10))
			.eval
			.assertEqualTo(value(10))

		term(value("Hello, world!"))
			.eval
			.assertEqualTo(value("Hello, world!"))

		fn(fn(term(DoublePlusValue)))
			.invoke(term(value(2)))
			.invoke(term(value(3)))
			.eval
			.assertEqualTo(value(5))

		fn(fn(term(DoubleMinusValue)))
			.invoke(term(value(5)))
			.invoke(term(value(3)))
			.eval
			.assertEqualTo(value(2))

		fn(fn(term(DoubleTimesValue)))
			.invoke(term(value(2)))
			.invoke(term(value(3)))
			.eval
			.assertEqualTo(value(6))

		fn(fn(term(StringPlusValue)))
			.invoke(term(value("Hello, ")))
			.invoke(term(value("world!")))
			.eval
			.assertEqualTo(value("Hello, world!"))

	}
}