package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test
import kotlin.test.fail

class EvaluatorTest {
	@Test
	fun resolveDefine() {
		evaluator()
			.plus(
				"define" lineTo script(
					"zero" lineTo script(),
					"gives" lineTo script("one")))
			.assertEqualTo(
				evaluator(
					context()
						.plus(
							function(
								pattern(script("zero")),
								body(script("one"))))))
	}

	@Test
	fun resolveApplyFunction() {
		val context =
			context()
				.plus(
					function(
						pattern(script("foo")),
						body(script("bar"))))
				.plus(
					function(
						pattern(script("zoo")),
						body(script("foo"))))

		evaluator(context)
			.plus("zoo" lineTo script())
			.assertEqualTo(
				evaluator(
					context,
					evaluated(script("bar" lineTo script()))))
	}

	@Test
	fun resolveApplyFunction_SOE() {
		val context =
			context()
				.plus(
					function(
						pattern(script("foo")),
						body(script("foo"))))

		try {
			evaluator(context).plus("foo" lineTo script())
			fail("StackOverflowError expected")
		} catch (soe: StackOverflowError) {

		}
	}

	@Test
	fun resolveAs() {
		evaluator(
			context(),
			evaluated(script("foo" lineTo script())))
			.plus("as" lineTo script("bar"))
			.assertEqualTo(
				evaluator(
					context().plus(binding(key(script("bar")), value(script("foo")))),
					evaluated(script())))
	}

	@Test
	fun resolveGetAs() {
		evaluator(
			context().plus(binding(key(script("foo")), value(script("bar")))))
			.plus("foo" lineTo script())
			.assertEqualTo(
				evaluator(
					context().plus(binding(key(script("foo")), value(script("bar")))),
					evaluated(script("bar"))))
	}
}