package leo25

import leo.base.assertEqualTo
import leo.base.assertNotNull
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import leo23.value.int
import kotlin.test.Test

class InterpreterTest {
	@Test
	fun literal() {
		script(line(literal("ok")))
			.interpret
			.assertEqualTo(script(line(literal("ok"))))
	}

	@Test
	fun name() {
		script("ok")
			.interpret
			.assertEqualTo(script("ok"))
	}

	@Test
	fun field() {
		script("foo" lineTo script("bar"))
			.interpret
			.assertEqualTo(script("foo" lineTo script("bar")))
	}

	@Test
	fun lines() {
		script(
			"foo" lineTo script(),
			"bar" lineTo script()
		)
			.interpret
			.assertEqualTo(
				script(
					"foo" lineTo script(),
					"bar" lineTo script()
				)
			)
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
	fun get() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			),
			getName lineTo script("x")
		)
			.interpret
			.assertEqualTo(script("x" lineTo script("zero")))

		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			),
			getName lineTo script("y")
		)
			.interpret
			.assertEqualTo(script("y" lineTo script("one")))

		script(
			"x" lineTo script("zero"),
			"y" lineTo script("one"),
			getName lineTo script("point")
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

		script(
			"x" lineTo script("zero"),
			getName lineTo script(
				"point" lineTo script(
					"y" lineTo script("one")
				)
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
	fun letEmpty() {
		script(
			"foo" lineTo script(),
			letName lineTo script("bar")
		)
			.interpret
			.assertEqualTo(
				script(
					"foo" lineTo script(),
					letName lineTo script("bar")
				)
			)
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
	fun switch() {
		script(
			"the" lineTo script(literal("Hello, ")),
			switchName lineTo script(
				"text" lineTo script(appendName lineTo script(literal("world!"))),
				"number" lineTo script(addName lineTo script(literal(2)))
			)
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))

		script(
			"the" lineTo script(literal(1)),
			switchName lineTo script(
				"text" lineTo script(appendName lineTo script(literal("world!"))),
				"number" lineTo script(addName lineTo script(literal(2)))
			)
		)
			.interpret
			.assertEqualTo(script(literal(3)))
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
						yesName lineTo script(
							becomeName lineTo script(line(literal("OK")))
						),
						noName lineTo script(
							becomeName lineTo script(
								getName lineTo script(numberName),
								subtractName lineTo script(line(literal(1))),
								repeatName lineTo script()
							)
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
						yesName lineTo script(
							becomeName lineTo script(line(literal("OK")))
						),
						noName lineTo script(
							becomeName lineTo script(
								getName lineTo script(numberName),
								subtractName lineTo script(line(literal(1))),
								recurseName lineTo script()
							)
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
			becomeName lineTo script(
				line(literal(1)),
				addName lineTo script(line(literal(2)))
			)
		)
			.interpret
			.assertEqualTo(
				script(line(literal(3)))
			)
	}

	@Test
	fun set() {
		script(
			""
		)
	}

	@Test
	fun private() {
		script(
			privateName lineTo script(
				letName lineTo script(
					"ping" lineTo script(),
					doName lineTo script("pong")
				)
			),
			"ping" lineTo script()
		)
			.interpret
			.assertEqualTo(script("pong"))
	}

	@Test
	fun private_double() {
		script(
			privateName lineTo script(
				privateName lineTo script(
					letName lineTo script(
						"ping" lineTo script(),
						doName lineTo script("pong")
					)
				)
			),
			"ping" lineTo script()
		)
			.interpret
			.assertEqualTo(script("ping"))
	}

	@Test
	fun use() {
		script(
			useName lineTo script(literal("lib"))
		)
			.interpret
			.assertEqualTo(script(errorName lineTo script(literal("lib (No such file or directory)"))))
	}

	@Test
	fun catch() {
		script(
			textName lineTo script("hello"),
			appendName lineTo script(textName lineTo script("world"))
		)
			.interpret
			.assertNotNull // TODO: Check for error.
	}

	@Test
	fun trace() {
		script(
			line(literal("Hello, ")),
			appendName lineTo script(line(literal("world!"))),
			traceName lineTo script()
		)
			.interpret
			.assertEqualTo(
				script(
					traceName lineTo script(
						resolveName lineTo script(literal("Hello, ")),
						resolveName lineTo script(literal("world!")),
						resolveName lineTo script(
							line(literal("Hello, ")),
							appendName lineTo script(line(literal("world!")))
						)
					)
				)
			)
	}
}
