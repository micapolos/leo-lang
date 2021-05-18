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

	@Test
	fun select() {
		value("x" to value("10"), "y" to value("20"), "x" to value("30"))
			.run {
				selectOrNull("x").assertEqualTo(value("x" to value("30")))
				selectOrNull("y").assertEqualTo(value("y" to value("20")))
				selectOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun get() {
		value("point" to value("x" to value("10"), "y" to value("20"), "x" to value("30")))
			.run {
				getOrNull("x").assertEqualTo(value("x" to value("30")))
				getOrNull("y").assertEqualTo(value("y" to value("20")))
				getOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun resolveGet() {
		value(
			"x" to value(
				"point" to value(
					"x" to value("10"),
					"y" to value("20")
				)
			)
		)
			.resolve
			.assertEqualTo(value("x" to value("10")))

		value(
			"y" to value(
				"point" to value(
					"x" to value("10"),
					"y" to value("20")
				)
			)
		)
			.resolve
			.assertEqualTo(value("y" to value("20")))

		value(
			"z" to value(
				"point" to value(
					"x" to value("10"),
					"y" to value("20")
				)
			)
		)
			.resolve
			.assertEqualTo(
				value(
					"z" to value(
						"point" to value(
							"x" to value("10"),
							"y" to value("20")
						)
					)
				)
			)
	}
}