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
			script("given" lineTo script()))
			.apply(program("foo" valueTo program()))
			.assertEqualTo(
				program(
					"given" valueTo program(
						"foo" valueTo program())))
	}

	@Test
	fun applyResolvesContext() {
		function(
			context(
				rule(
					pattern(
						program(
							"foo" valueTo program())),
					body(
						program(
							"bar" valueTo program())))),
			script("foo" lineTo script()))
			.apply(program("goo" valueTo program()))
			.assertEqualTo(program("bar"))
	}
}