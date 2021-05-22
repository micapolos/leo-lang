package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ValueTest {
	@Test
	fun values() {
		value("foo")
		value(
			"point" lineTo value(
				"first" lineTo value("foo"),
				"last" lineTo value("bar")
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
			"x" lineTo value("zero"),
			"y" lineTo value("one"),
			"x" lineTo value("two")
		)
			.run {
				selectOrNull("x").assertEqualTo(value("x" lineTo value("two")))
				selectOrNull("y").assertEqualTo(value("y" lineTo value("one")))
				selectOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun get() {
		value("point" lineTo value("x" lineTo value("10"), "y" lineTo value("20"), "x" lineTo value("30")))
			.run {
				getOrNull("x").assertEqualTo(value("x" lineTo value("30")))
				getOrNull("y").assertEqualTo(value("y" lineTo value("20")))
				getOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun resolveGet() {
		value(
			"point" lineTo value(
				"x" lineTo value("10"),
				"y" lineTo value("20")
			),
			getName lineTo value("x")
		)
			.resolve
			.assertEqualTo(value("x" lineTo value("10")))

		value(
			"point" lineTo value(
				"x" lineTo value("10"),
				"y" lineTo value("20")
			),
			getName lineTo value("y")
		)
			.resolve
			.assertEqualTo(value("y" lineTo value("20")))
	}

	@Test
	fun resolveFunction() {
		value(line(dictionary().function(body(script("foo")))))
			.resolveFunctionOrNull
			.assertEqualTo(dictionary().function(body(script("foo"))))

		value("function" lineTo value("foo"))
			.resolveFunctionOrNull
			.assertEqualTo(null)
	}

	@Test
	fun resolveText() {
		value(line(literal("foo")))
			.textOrNull
			.assertEqualTo("foo")

		value("foo")
			.textOrNull
			.assertEqualTo(null)

		value("text" lineTo value("foo"))
			.resolveFunctionOrNull
			.assertEqualTo(null)
	}

	@Test
	fun resolveFunctionApply() {
		value(
			line(dictionary().function(body(script(getName lineTo script("name"))))),
			applyName lineTo value("name" lineTo value("foo"))
		)
			.resolveFunctionApplyOrNull
			.assertEqualTo(value("name" lineTo value("foo")))
	}
}