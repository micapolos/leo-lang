package leo14.untyped

import leo.base.assertEqualTo
import leo14.script
import kotlin.test.Test

class ContextTest {
	private val context = context(
		rule(pattern(thunk(value("x"))), body(thunk(value("zero")))),
		rule(pattern(thunk(value("y"))), body(thunk(value("one")))),
		rule(pattern(thunk(value("x"))), body(thunk(value("two")))))

	@Test
	fun apply_rules() {
		context
			.apply(thunk(value("x")))
			.assertEqualTo(thunk(value("two")))

		context
			.apply(thunk(value("y")))
			.assertEqualTo(thunk(value("one")))

		context
			.apply(thunk(value("z")))
			.assertEqualTo(null)
	}

	@Test
	fun compile_is() {
		context
			.compile(
				thunk(
					value(
						"foo" lineTo value(),
						givesName lineTo value("bar"))))
			.assertEqualTo(
				context.push(
					rule(
						pattern(thunk(value("foo"))),
						body(thunk(value("bar"))))))
	}

	@Test
	fun compile_does() {
		context
			.compile(
				thunk(
					value(
						"foo" lineTo value(),
						doesName lineTo value("bar"))))
			.assertEqualTo(
				context.push(
					rule(
						pattern(thunk(value("foo"))),
						body(script("bar")))))
	}
}