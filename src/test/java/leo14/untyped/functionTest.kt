package leo14.untyped

import leo.base.assertEqualTo
import kotlin.test.Test

class FunctionTest {
	@Test
	fun applyAppends() {
		function(
			context(),
			program("bar" valueTo program()))
			.apply(program("foo" valueTo program()))
			.assertEqualTo(
				program(
					"foo" valueTo program(),
					"bar" valueTo program()))
	}

	@Test
	fun applyResolvesContext() {
		function(
			context(
				rule(
					pattern(
						program(
							"foo" valueTo program(),
							"bar" valueTo program())),
					body(
						program(
							"zoo" valueTo program())))),
			program("bar" valueTo program()))
			.apply(program("foo" valueTo program()))
			.assertEqualTo(program("zoo"))
	}
}