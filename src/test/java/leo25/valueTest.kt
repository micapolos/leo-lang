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
	fun resolveGetOrMake() {
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

		value(
			"point" lineTo value(
				"x" lineTo value("10"),
				"y" lineTo value("20")
			),
			"z" lineTo value(
			)
		)
			.resolve
			.assertEqualTo(
				value(
					"z" lineTo value(
						"point" lineTo value(
							"x" lineTo value("10"),
							"y" lineTo value("20")
						)
					)
				)
			)
	}

	@Test
	fun resolveFunction() {
		value(line(Function(context(), script("foo"))))
			.resolveFunctionOrNull
			.assertEqualTo(Function(context(), script("foo")))

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
			line(Function(context(), script("given"))),
			"apply" lineTo value("foo")
		)
			.resolveFunctionApplyOrNull
			.assertEqualTo(value("given" lineTo value("foo")))
	}

	@Test
	fun resolveTextPlusText() {
		value(
			line(literal("Hello, ")),
			"plus" lineTo value(line(literal("world!")))
		)
			.resolveTextPlusTextOrNull
			.assertEqualTo(value(line(literal("Hello, world!"))))
	}
}