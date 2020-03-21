package leo14.untyped

import leo.base.assertEqualTo
import leo14.script
import kotlin.test.Test

class ContextTest {
	private val context = context(
		pattern(program("x")) ruleTo body(constant(program("zero"))),
		pattern(program("y")) ruleTo body(constant(program("one"))),
		pattern(program("x")) ruleTo body(constant(program("two"))))

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
			.assertEqualTo(program(value(function(context, script("foo")))))
	}

	@Test
	fun compile_is() {
		context
			.compile(
				program(
					"foo" valueTo program(),
					isName valueTo program("bar")))
			.assertEqualTo(
				context.push(
					rule(
						pattern(program("foo")),
						body(constant(program("bar"))))))
	}

	@Test
	fun compile_does() {
		context
			.compile(
				program(
					"foo" valueTo program(),
					givesName valueTo program("bar")))
			.assertEqualTo(
				context.push(
					rule(
						pattern(program("foo")),
						body(function(context, script("bar"))))))
	}
}