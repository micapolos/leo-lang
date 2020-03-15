package leo14.untyped

import leo.base.assertEqualTo
import kotlin.test.Test

class ContextTest {
	private val context = context(
		pattern(program("x")) ruleTo body(program("zero")),
		pattern(program("y")) ruleTo body(program("one")),
		pattern(program("x")) ruleTo body(program("two")))

	@Test
	fun apply_rules() {
		context
			.apply(program("x"))
			.assertEqualTo(program("two"))

		context
			.apply(program("y"))
			.assertEqualTo(program("one"))

		context
			.apply(program("z"))
			.assertEqualTo(null)
	}

	@Test
	fun apply_function() {
		context
			.apply(program("function" valueTo program("foo")))
			.assertEqualTo(program(value(function(context, program("foo")))))
	}

	@Test
	fun apply_eval() {
		context
			.apply(
				program(
					"x" valueTo program(),
					"eval" valueTo program()))
			.assertEqualTo(context.eval(program("x")))
	}

	@Test
	fun compile_gives() {
		context
			.compile(
				program(
					"foo" valueTo program(),
					"gives" valueTo program("bar")))
			.assertEqualTo(
				context.push(
					rule(
						pattern(program("foo")),
						body(program("bar")))))
	}

	@Test
	fun compile_does() {
		context
			.compile(
				program(
					"foo" valueTo program(),
					"does" valueTo program("bar")))
			.assertEqualTo(
				context.push(
					rule(
						pattern(program("foo")),
						body(function(context, program("bar"))))))
	}
}