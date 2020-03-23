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
			.apply(program("x"))
			.assertEqualTo(thunk(program("two")))

		context
			.apply(program("y"))
			.assertEqualTo(thunk(program("one")))

		context
			.apply(program("z"))
			.assertEqualTo(null)
	}

	@Test
	fun compile_is() {
		context
			.compile(
				program(
					"foo" valueTo program(),
					givesName valueTo program("bar")))
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
				program(
					"foo" valueTo program(),
					doesName valueTo program("bar")))
			.assertEqualTo(
				context.push(
					rule(
						pattern(program("foo")),
						body(script("bar")))))
	}
}