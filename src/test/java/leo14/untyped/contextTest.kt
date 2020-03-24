package leo14.untyped

import leo.base.assertEqualTo
import leo14.script
import kotlin.test.Test

class ContextTest {
	private val context = context(
		rule(pattern(program("x")), body(thunk(program("zero")))),
		rule(pattern(program("y")), body(thunk(program("one")))),
		rule(pattern(program("x")), body(thunk(program("two")))))

	@Test
	fun apply_rules() {
		context
			.apply(thunk(program("x")))
			.assertEqualTo(thunk(program("two")))

		context
			.apply(thunk(program("y")))
			.assertEqualTo(thunk(program("one")))

		context
			.apply(thunk(program("z")))
			.assertEqualTo(null)
	}

	@Test
	fun compile_is() {
		context
			.compile(
				thunk(
					program(
						"foo" lineTo program(),
						givesName lineTo program("bar"))))
			.assertEqualTo(
				context.push(
					rule(
						pattern(program("foo")),
						body(thunk(program("bar"))))))
	}

	@Test
	fun compile_does() {
		context
			.compile(
				thunk(
					program(
						"foo" lineTo program(),
						doesName lineTo program("bar"))))
			.assertEqualTo(
				context.push(
					rule(
						pattern(program("foo")),
						body(script("bar")))))
	}
}