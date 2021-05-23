package leo25

import leo.base.assertEqualTo
import leo.base.assertTrue
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ValueTest {
	@Test
	fun values() {
		value("foo")
		value(
			"point" fieldTo value(
				"first" fieldTo value("foo"),
				"last" fieldTo value("bar")
			)
		)
	}

	@Test
	fun dictionaryResolve() {
		dictionary()
			.resolve(value("foo"))
			.assertEqualTo(value("foo"))
	}

	@Test
	fun select() {
		value(
			"x" fieldTo value("zero"),
			"y" fieldTo value("one"),
			"x" fieldTo value("two")
		)
			.run {
				selectOrNull("x").assertEqualTo(value("x" fieldTo value("two")))
				selectOrNull("y").assertEqualTo(value("y" fieldTo value("one")))
				selectOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun get() {
		value(
			"point" fieldTo value(
				"x" fieldTo value("10"),
				"y" fieldTo value("20"),
				"x" fieldTo value("30")
			)
		)
			.run {
				getOrNull("x").assertEqualTo(value("x" fieldTo value("30")))
				getOrNull("y").assertEqualTo(value("y" fieldTo value("20")))
				getOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun resolveGet() {
		value(
			"point" fieldTo value(
				"x" fieldTo value("10"),
				"y" fieldTo value("20")
			),
			getName fieldTo value("x")
		)
			.resolve
			.assertEqualTo(value("x" fieldTo value("10")))

		value(
			"point" fieldTo value(
				"x" fieldTo value("10"),
				"y" fieldTo value("20")
			),
			getName fieldTo value("y")
		)
			.resolve
			.assertEqualTo(value("y" fieldTo value("20")))

		value(
			"point" fieldTo value(
				"x" fieldTo value("10"),
				"y" fieldTo value("20")
			),
			getName fieldTo value("x" fieldTo value("30"))
		)
			.resolve
			.assertEqualTo(value("x" fieldTo value("30")))

		value(
			"point" fieldTo value(
				"x" fieldTo value("10"),
				"y" fieldTo value("20")
			),
			getName fieldTo value("y" fieldTo value("30"))
		)
			.resolve
			.assertEqualTo(value("y" fieldTo value("30")))

		value(
			"x" fieldTo value("10"),
			"y" fieldTo value("20"),
			getName fieldTo value("point")
		)
			.resolve
			.assertEqualTo(
				value(
					"point" fieldTo value(
						"x" fieldTo value("10"),
						"y" fieldTo value("20")
					)
				)
			)

		value(
			"x" fieldTo value("10"),
			getName fieldTo value(
				"point" fieldTo value(
					"y" fieldTo value("20")
				)
			)
		)
			.resolve
			.assertEqualTo(
				value(
					"point" fieldTo value(
						"x" fieldTo value("10"),
						"y" fieldTo value("20")
					)
				)
			)
	}

	@Test
	fun resolveFunction() {
		value(field(dictionary().function(body(script("foo")))))
			.functionOrNull
			.assertEqualTo(dictionary().function(body(script("foo"))))

		value("function" fieldTo value("foo"))
			.functionOrNull
			.assertEqualTo(null)
	}

	@Test
	fun resolveText() {
		value(field(literal("foo")))
			.textOrNull
			.assertEqualTo("foo")

		value("foo")
			.textOrNull
			.assertEqualTo(null)

		value("text" fieldTo value("foo"))
			.functionOrNull
			.assertEqualTo(null)
	}

	@Test
	fun resolveFunctionApply() {
		value(
			field(dictionary().function(body(script(getName lineTo script("name"))))),
			applyName fieldTo value("name" fieldTo value("foo"))
		)
			.resolveFunctionApplyOrNull
			.assertEqualTo(value("name" fieldTo value("foo")))
	}
}