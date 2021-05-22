package leo25

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class InterpreterTest {
	@Test
	fun make() {
		script(
			"red" lineTo script(),
			makeName lineTo script("color")
		)
			.interpret
			.assertEqualTo(script("color" lineTo script("red")))
	}

	@Test
	fun textPlusTest() {
		script(
			line(literal("Hello, ")),
			"append" lineTo script(literal("world!"))
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun struct() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			)
		)
			.interpret
			.assertEqualTo(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one")
					)
				)
			)
	}

	@Test
	fun function() {
		script("function" lineTo script("foo"))
			.interpret
			.assertEqualTo(script("function" lineTo script("foo")))
	}

	@Test
	fun functionApply() {
		script(
			doingName lineTo script(getName lineTo script("name")),
			applyName lineTo script("name" lineTo script("foo"))
		)
			.interpret
			.assertEqualTo(script("name" lineTo script("foo")))
	}

	@Test
	fun define() {
		script(
			"foo" lineTo script(),
			letName lineTo script("bar")
		)
			.interpret
			.assertEqualTo(script("foo"))
	}

	@Test
	fun defineGives() {
		script(
			letName lineTo script(
				"name" lineTo script("anything"),
				doName lineTo script("name")
			),
			"name" lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script("name" lineTo script("foo")))
	}

	@Test
	fun defineIs() {
		script(
			letName lineTo script(
				"foo" lineTo script(),
				isName lineTo script("given")
			),
			"foo" lineTo script()
		)
			.interpret
			.assertEqualTo(script("given"))
	}

	@Test
	fun switch() {
		script(
			"shape" lineTo script(
				"circle" lineTo script(
					"radius" lineTo script("zero")
				)
			),
			switchName lineTo script(
				"circle" lineTo script(getName lineTo script("radius")),
				"rectangle" lineTo script(getName lineTo script("height"))
			)
		)
			.interpret
			.assertEqualTo(script("radius" lineTo script("zero")))

		script(
			"shape" lineTo script(
				"rectangle" lineTo script(
					"height" lineTo script("zero")
				)
			),
			switchName lineTo script(
				"circle" lineTo script(getName lineTo script("radius")),
				"rectangle" lineTo script(getName lineTo script("height"))
			)
		)
			.interpret
			.assertEqualTo(script("height" lineTo script("zero")))

		script(
			"shape" lineTo script(
				"circle" lineTo script(
					"radius" lineTo script("zero")
				)
			),
			switchName lineTo script(
				"triangle" lineTo script(getName lineTo script("height"))
			)
		)
			.interpret
			.assertEqualTo(
				script(
					"shape" lineTo script(
						"circle" lineTo script(
							"radius" lineTo script("zero")
						)
					),
					switchName lineTo script(
						"triangle" lineTo script(getName lineTo script("height"))
					)
				)
			)
	}

	@Test
	fun hash() {
		script(
			"foo" lineTo script(),
			"hash" lineTo script()
		)
			.interpret
			.assertEqualTo(script("hash" lineTo script(literal(value("foo").hashCode()))))
	}

	@Test
	fun equals() {
		script(
			"foo" lineTo script(),
			"equals" lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script("boolean" lineTo script("true")))

		script(
			"foo" lineTo script(),
			"equals" lineTo script("bar")
		)
			.interpret
			.assertEqualTo(script("boolean" lineTo script("false")))

		script(
			line(literal("foo")),
			"equals" lineTo script(line(literal("foo")))
		)
			.interpret
			.assertEqualTo(script("boolean" lineTo script("true")))
	}

	@Test
	fun repeat() {
		script(
			line(literal(10000)),
			doName lineTo script(
				"get" lineTo script("number"),
				"equals" lineTo script(line(literal(0))),
				"switch" lineTo script(
					"true" lineTo script(line(literal("OK"))),
					"false" lineTo script(
						"get" lineTo script("number"),
						"subtract" lineTo script(line(literal(1))),
						"repeat" lineTo script()
					)
				)
			)
		)
			.interpret
			.assertEqualTo(script(line(literal("OK"))))
	}
}
