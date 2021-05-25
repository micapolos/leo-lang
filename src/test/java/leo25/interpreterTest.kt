package leo25

import leo.base.assertEqualTo
import leo.base.assertNotNull
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
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
			"foo" lineTo script("bar"),
			"zoo" lineTo script("zar")
		)
			.interpret
			.assertEqualTo(
				script(
					"foo" lineTo script("bar"),
					"zoo" lineTo script("zar")
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
	fun make_implicit() {
		script(
			"red" lineTo script(),
			"color" lineTo script()
		)
			.interpret
			.assertEqualTo(script("color" lineTo script("red")))
	}

	@Test
	fun get_implicit() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			),
			"x" lineTo script()
		)
			.interpret
			.assertEqualTo(script("x" lineTo script("zero")))

		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			),
			"y" lineTo script()
		)
			.interpret
			.assertEqualTo(script("y" lineTo script("one")))
	}

	@Test
	fun get() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			),
			"x" lineTo script()
		)
			.interpret
			.assertEqualTo(script("x" lineTo script("zero")))

		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			),
			"y" lineTo script()
		)
			.interpret
			.assertEqualTo(script("y" lineTo script("one")))

		script(
			"x" lineTo script("zero"),
			"y" lineTo script("one"),
			"point" lineTo script()
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
	fun apply() {
		script(
			doingName lineTo script("name"),
			giveName lineTo script("name" lineTo script("foo"))
		)
			.interpret
			.assertEqualTo(script("name" lineTo script("foo")))
	}

	@Test
	fun take() {
		script(
			"name" lineTo script("foo"),
			takeName lineTo script(doingName lineTo script("name"))
		)
			.interpret
			.assertEqualTo(script("name" lineTo script("foo")))
	}

	@Test
	fun apply_error() {
		script(
			"foo" lineTo script(),
			giveName lineTo script("bar")
		)
			.interpret
			.assertEqualTo(
				script(
					errorName lineTo script(
						"foo" lineTo script(),
						"not" lineTo script("function")
					)
				)
			)
	}

	@Test
	fun functionGet() {
		script(
			"map" lineTo script(doingName lineTo script("foo")),
			doingName lineTo script()
		)
			.interpret
			.assertEqualTo(script(doingName lineTo script("foo")))
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
				doName lineTo script("name" lineTo script())
			),
			"name" lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script("name" lineTo script("foo")))
	}

	@Test
	fun letName() {
		script(
			letName lineTo script(
				numberName lineTo script(anyName),
				"increment" lineTo script(),
				doName lineTo script(
					numberName lineTo script(),
					plusName lineTo script(literal(1))
				)
			),
			line(literal(2)),
			"increment" lineTo script()
		)
			.interpret
			.assertEqualTo(script(literal(3)))
	}

	@Test
	fun switch() {
		script(
			"the" lineTo script(literal("Hello, ")),
			switchName lineTo script(
				"text" lineTo script(appendName lineTo script(literal("world!"))),
				"number" lineTo script(plusName lineTo script(literal(2)))
			)
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))

		script(
			"the" lineTo script(literal(1)),
			switchName lineTo script(
				"text" lineTo script(appendName lineTo script(literal("world!"))),
				"number" lineTo script(plusName lineTo script(literal(2)))
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
					numberName lineTo script(),
					equalsName lineTo script(line(literal(0))),
					switchName lineTo script(
						yesName lineTo script(
							becomeName lineTo script(line(literal("OK")))
						),
						noName lineTo script(
							becomeName lineTo script(
								numberName lineTo script(),
								minusName lineTo script(line(literal(1))),
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
					numberName lineTo script(),
					equalsName lineTo script(line(literal(0))),
					switchName lineTo script(
						yesName lineTo script(
							becomeName lineTo script(line(literal("OK")))
						),
						noName lineTo script(
							becomeName lineTo script(
								numberName lineTo script(),
								minusName lineTo script(line(literal(1))),
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
			scriptName lineTo script(hashName),
			evaluateName lineTo script()
		)
			.interpret
			.assertEqualTo(script(hashName lineTo script(line(literal(value().hashCode())))))
	}

	@Test
	fun evaluate_with() {
		script(
			scriptName lineTo script(
				line(literal("Hello, ")),
				appendName lineTo script(textName lineTo script())
			),
			evaluateName lineTo script(literal("world!"))
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun comment() {
		script(
			commentName lineTo script("first" lineTo script("number")),
			line(literal(2)),
			commentName lineTo script("second" lineTo script("number")),
			plusName lineTo script(line(literal(3))),
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
				plusName lineTo script(line(literal(2)))
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
		script(useName lineTo script("lib" lineTo script("text")))
			.interpret
			.assertEqualTo(script(errorName lineTo script(literal("lib/text.leo (No such file or directory)"))))
	}

	@Test
	fun error_native() {
		script(
			textName lineTo script("hello"),
			plusName lineTo script(textName lineTo script("world"))
		)
			.interpret
			.assertNotNull // TODO: Check for error.
	}

	@Test
	fun trace() {
		environment(traceOrNull = emptyTrace)
			.interpret(
				script(
					line(literal("Hello, ")),
					plusName lineTo script(line(literal("world!"))),
					traceName lineTo script()
				)
			)
			.assertEqualTo(
				script(
					traceName lineTo script(
						resolveName lineTo script(literal("Hello, ")),
						resolveName lineTo script(literal("world!")),
						resolveName lineTo script(
							line(literal("Hello, ")),
							plusName lineTo script(line(literal("world!")))
						)
					)
				)
			)
	}

	@Test
	fun trace_disabled() {
		environment()
			.interpret(
				script(
					line(literal("Hello, ")),
					plusName lineTo script(line(literal("world!"))),
					traceName lineTo script()
				)
			)
			.assertEqualTo(script(traceName lineTo script(disabledName)))
	}

	@Test
	fun getHash() {
		script(
			"foo" lineTo script(),
			hashName lineTo script()
		)
			.interpret
			.assertEqualTo(script(hashName lineTo script(literal(value("foo").hashCode()))))
	}

	@Test
	fun equals_() {
		script(
			"foo" lineTo script(),
			equalsName lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script(equalsName lineTo script(yesName)))

		script(
			"foo" lineTo script(),
			equalsName lineTo script("bar")
		)
			.interpret
			.assertEqualTo(script(equalsName lineTo script(noName)))

		script(
			line(literal("foo")),
			equalsName lineTo script(line(literal("foo")))
		)
			.interpret
			.assertEqualTo(script(equalsName lineTo script(yesName)))
	}

	@Test
	fun fail() {
		script(
			failName lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script(errorName lineTo script("foo")))
	}

	@Test
	fun try_success() {
		script(
			tryName lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script(tryName lineTo script(successName lineTo script("foo"))))
	}

	@Test
	fun try_error() {
		script(
			tryName lineTo script(
				failName lineTo script("foo")
			)
		)
			.interpret
			.assertEqualTo(script(tryName lineTo script(errorName lineTo script("foo"))))
	}

	@Test
	fun test_success() {
		script(
			testName lineTo script(
				line(literal(2)),
				plusName lineTo script(literal(2)),
				equalsName lineTo script(literal(4))
			)
		)
			.interpret
			.assertEqualTo(script())
	}

	@Test
	fun test_error() {
		script(
			testName lineTo script(
				line(literal(2)),
				plusName lineTo script(literal(2)),
				equalsName lineTo script(literal(5))
			)
		)
			.interpret
			.assertEqualTo(
				script(
					errorName lineTo script(
						testName lineTo script(
							line(literal(2)),
							plusName lineTo script(literal(2)),
							equalsName lineTo script(literal(5))
						),
						causeName lineTo script(
							line(literal(4)),
							notName lineTo script(
								equalsName lineTo script(literal(5))
							)
						)
					)
				)
			)
	}

	@Test
	fun is_() {
		script(
			line(literal("foo")),
			isName lineTo script(textName lineTo script(anyName))
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(yesName)))

		script(
			line(literal("doo")),
			isName lineTo script(numberName lineTo script(anyName))
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(noName)))
	}
}
