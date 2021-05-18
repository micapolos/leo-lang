package leo25

import leo.base.assertEqualTo
import kotlin.test.Test

class ValueTest {
	@Test
	fun values() {
		value("Michał Pociecha-Łoś")
		value(
			"name" to value(
				"first" to value("Michał"),
				"last" to value("Pociecha-Łoś")
			)
		)
	}

	@Test
	fun contextResolve() {
		context()
			.resolve(value("Michał"))
			.assertEqualTo(value("Michał"))
	}
}