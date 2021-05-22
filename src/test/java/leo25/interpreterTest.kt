package leo25

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import leo23.value.int
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
	fun defineBe() {
		script(
			letName lineTo script(
				"foo" lineTo script(),
				beName lineTo script("bar")
			),
			getName lineTo script("foo" lineTo script())
		)
			.interpret
			.assertEqualTo(script("bar"))
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
			getName lineTo script(hashName lineTo script())
		)
			.interpret
			.assertEqualTo(script(hashName lineTo script(literal(value("foo").hashCode()))))
	}

	@Test
	fun isEqual() {
		script(
			"foo" lineTo script(),
			isName lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(yesName)))

		script(
			"foo" lineTo script(),
			isName lineTo script("bar")
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(noName)))

		script(
			line(literal("foo")),
			isName lineTo script(line(literal("foo")))
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(yesName)))
	}

	@Test
	fun doRepeating() {
		script(
			line(literal(10000)),
			doName lineTo script(
				repeatingName lineTo script(
					getName lineTo script(numberName),
					isName lineTo script(line(literal(0))),
					switchName lineTo script(
						yesName lineTo script(line(literal("OK"))),
						noName lineTo script(
							getName lineTo script(numberName),
							"subtract" lineTo script(line(literal(1))),
							repeatName lineTo script()
						)
					)
				)
			)
		)
			.interpret
			.assertEqualTo(script(line(literal("OK"))))
	}

	@Test
	fun doRecursing() {
		script(
			line(literal(100)),
			doName lineTo script(
				recursingName lineTo script(
					getName lineTo script(numberName),
					isName lineTo script(line(literal(0))),
					switchName lineTo script(
						yesName lineTo script(line(literal("OK"))),
						noName lineTo script(
							getName lineTo script(numberName),
							"subtract" lineTo script(line(literal(1))),
							recurseName lineTo script()
						)
					)
				)
			)
		)
			.interpret
			.assertEqualTo(script(line(literal("OK"))))
	}

	@Test
	fun script_() {
		script(scriptName lineTo script(hashName))
			.interpret
			.assertEqualTo(script("hash"))
	}

	@Test
	fun evaluate() {
		script(
			scriptName lineTo script(getName lineTo script(hashName)),
			evaluateName lineTo script()
		)
			.interpret
			.assertEqualTo(script(hashName lineTo script(line(literal(value().hashCode())))))
	}

	@Test
	fun textAppendText() {
		script(
			line(literal("Hello, ")),
			"append" lineTo script(line(literal("world!")))
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun numberAddNumber() {
		script(
			line(literal(2)),
			"add" lineTo script(line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(5)))
	}

	@Test
	fun numberSubtractNumber() {
		script(
			line(literal(5)),
			"subtract" lineTo script(line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(2)))
	}

	@Test
	fun numberMultiplyByNumber() {
		script(
			line(literal(2)),
			"multiply" lineTo script("by" lineTo script(line(literal(3))))
		)
			.interpret
			.assertEqualTo(script(literal(6)))
	}
}
