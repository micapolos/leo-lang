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
	fun letDo() {
		script(
			letName lineTo script(
				"name" lineTo script(anyName),
				doName lineTo script(getName lineTo script("name" lineTo script()))
			),
			"name" lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script("name" lineTo script("foo")))
	}

	@Test
	fun letBe() {
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
							subtractName lineTo script(line(literal(1))),
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
							subtractName lineTo script(line(literal(1))),
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
			.assertEqualTo(script(hashName))
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
	fun comment() {
		script(
			commentName lineTo script("first" lineTo script("number")),
			line(literal(2)),
			commentName lineTo script("second" lineTo script("number")),
			addName lineTo script(line(literal(3))),
			commentName lineTo script("expecting" lineTo script(literal(5)))
		)
			.interpret
			.assertEqualTo(script(literal(5)))
	}

	@Test
	fun be() {
		script(
			"zero" lineTo script(),
			beName lineTo script(
				line(literal(1)),
				addName lineTo script(line(literal(2)))
			)
		)
			.interpret
			.assertEqualTo(
				script(line(literal(3)))
			)
	}
}
