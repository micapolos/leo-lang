package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun applyAppends() {
		function(
			context(),
			script("bar" lineTo script()))
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
			script("bar" lineTo script()))
			.apply(program("foo" valueTo program()))
			.assertEqualTo(program("zoo"))
	}
}