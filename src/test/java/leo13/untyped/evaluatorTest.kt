package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class evaluatorTest {
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
								body(context(), script("one"))))))
	}

	@Test
	fun resolveApplyFunction() {
		val context =
			context()
				.plusFunction { context ->
					function(
						pattern(script("foo")),
						body(context, script("bar")))
				}

		evaluator(context)
			.plus("foo" lineTo script())
			.assertEqualTo(
				evaluator(
					context,
					evaluated(script("bar" lineTo script()))))
	}

	@Test
	fun resolveApplyFunction_chaining() {
		val context =
			context()
				.plusFunction { context ->
					function(
						pattern(script("foo")),
						body(context, script("bar")))
				}
				.plusFunction { context ->
					function(
						pattern(script("zoo")),
						body(context, script("foo")))
				}

		evaluator(context)
			.plus("zoo" lineTo script())
			.assertEqualTo(
				evaluator(
					context,
					evaluated(script("bar" lineTo script()))))
	}

	@Test
	fun resolveApplyFunction_noImplicitRecursion() {
		val context =
			context()
				.plusFunction { context ->
					function(
						pattern(script("foo")),
						body(context, script("foo")))
				}

		evaluator(context)
			.plus("foo" lineTo script())
			.assertEqualTo(evaluator(context, evaluated(script("foo"))))
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

	@Test
	fun resolveMeta() {
		val context = context().plus(binding(key(script("zero")), value(script("one"))))

		evaluator(context)
			.plus("zero" lineTo script())
			.assertEqualTo(
				evaluator(context, evaluated(script("one"))))

		evaluator(context)
			.plus("meta" lineTo script("zero" lineTo script()))
			.assertEqualTo(
				evaluator(context, evaluated(script("zero"))))

		evaluator(context)
			.plus("meta" lineTo script("meta" lineTo script("zero")))
			.assertEqualTo(
				evaluator(context, evaluated(script("meta" lineTo script("one")))))

		evaluator(context)
			.plus("foo" lineTo script())
			.plus("meta" lineTo script("bar"))
			.assertEqualTo(
				evaluator(
					context,
					evaluated(
						script(
							"foo" lineTo script(),
							"bar" lineTo script()))))
	}
}