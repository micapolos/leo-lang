package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class LazyTest {
	@Test
	fun value_() {
		lazy(scope(), script("minus" lineTo script(literal(1))))
			.value
			.assertEqualTo(value(literal(-1)))
	}

	@Test
	fun either() {
		lazy(
			scope(),
			script(
				"either" lineTo script(
					"stop" lineTo script(),
					"foo" lineTo script("recurse"))))
			.value
			.assertEqualTo(
				value(
					"either" lineTo value(
						"stop" lineTo value(),
						"foo" lineTo value("recurse"))))
	}
}