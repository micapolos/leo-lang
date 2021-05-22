package leo25

import leo.base.assertEqualTo
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
	fun contextResolve() {
		context()
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
			"x" lineTo value()
		)
			.resolve
			.assertEqualTo(value("x" lineTo value("10")))

		value(
			"point" lineTo value(
				"x" lineTo value("10"),
				"y" lineTo value("20")
			),
			"y" lineTo value()
		)
			.resolve
			.assertEqualTo(value("y" lineTo value("20")))
	}

	@Test
	fun resolveFunction() {
		value(line(context().function(body(script("foo")))))
			.resolveFunctionOrNull
			.assertEqualTo(context().function(body(script("foo"))))

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
			line(context().function(body(script("name")))),
			takeName lineTo value("name" lineTo value("foo"))
		)
			.resolveFunctionApplyOrNull
			.assertEqualTo(value("name" lineTo value("foo")))
	}

	@Test
	fun resolveTextPlusText() {
		value(
			line(literal("Hello, ")),
			"plus" lineTo value(line(literal("world!")))
		)
			.resolve
			.assertEqualTo(value(line(literal("Hello, world!"))))
	}

	@Test
	fun resolveNumberMinusNumber() {
		value(
			line(literal(5)),
			"minus" lineTo value(line(literal(3)))
		)
			.resolve
			.assertEqualTo(value(line(literal(2))))
	}
}