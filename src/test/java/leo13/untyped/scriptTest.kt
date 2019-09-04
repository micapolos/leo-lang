package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ScriptTest {
	@Test
	fun resolveLine() {
		script()
			.resolve("foo" lineTo script())
			.assertEqualTo(script("foo" lineTo script()))
	}

	@Test
	fun normalizedLineOrNull() {
		script("red" lineTo script())
			.normalizedLineOrNull("color" lineTo script())
			.assertEqualTo("color" lineTo script("red"))

		script("red" lineTo script())
			.normalizedLineOrNull("color" lineTo script("blue"))
			.assertEqualTo(null)
	}

	@Test
	fun resolveGet() {
		script()
			.resolve(
				"x" lineTo script(
					"vec" lineTo script(
						"x" lineTo script("one"),
						"y" lineTo script("two"))))
			.assertEqualTo(script("x" lineTo script("one")))

		script()
			.resolve(
				"y" lineTo script(
					"vec" lineTo script(
						"x" lineTo script("one"),
						"y" lineTo script("two"))))
			.assertEqualTo(script("y" lineTo script("two")))
	}

	@Test
	fun resolveSet() {
		script(
			"vec" lineTo script(
				"x" lineTo script("one"),
				"y" lineTo script("two")))
			.resolve(
				"set" lineTo script(
					"x" lineTo script("ten"),
					"y" lineTo script("eleven")))
			.assertEqualTo(
				script(
					"vec" lineTo script(
						"x" lineTo script("ten"),
						"y" lineTo script("eleven"))))
	}

	@Test
	fun setFirstRhsOrNull() {
		script(
			"x" lineTo script("one"),
			"y" lineTo script("two"))
			.setFirstRhsOrNull("x" lineTo script("ten"))
			.assertEqualTo(
				script(
					"x" lineTo script("ten"),
					"y" lineTo script("two")))

		script(
			"x" lineTo script("one"),
			"y" lineTo script("two"))
			.setFirstRhsOrNull("y" lineTo script("ten"))
			.assertEqualTo(
				script(
					"x" lineTo script("one"),
					"y" lineTo script("ten")))
	}

	@Test
	fun caseOrNull() {
		script("zero" lineTo script("foo"))
			.caseOrNull(
				script(
					"case" lineTo script("zero" lineTo script("ten")),
					"case" lineTo script("one" lineTo script("eleven"))))
			.assertEqualTo(script("zero" lineTo script("ten")))

		script("one" lineTo script("foo"))
			.caseOrNull(
				script(
					"case" lineTo script("zero" lineTo script("ten")),
					"case" lineTo script("one" lineTo script("eleven"))))
			.assertEqualTo(script("one" lineTo script("eleven")))

		script("two" lineTo script("foo"))
			.caseOrNull(
				script(
					"case" lineTo script("zero" lineTo script("ten")),
					"case" lineTo script("one" lineTo script("eleven"))))
			.assertEqualTo(null)
	}

	@Test
	fun resolveSwitchOrNull() {
		script(
			"bit" lineTo script(
				"zero" lineTo script(
					"foo")))
			.resolveCaseOrNull(
				script(
					"case" lineTo script("zero" lineTo script("ten")),
					"case" lineTo script("one" lineTo script("eleven"))))
			.assertEqualTo(script("zero" lineTo script("ten")))
	}

	@Test
	fun matchesAnything() {
		script("foo" lineTo script("bar"))
			.matches(script("anything"))
			.assertEqualTo(true)
	}

	@Test
	fun matchesDeep() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")))
			.matches(
				script(
					"vec" lineTo script(
						"x" lineTo script("anything"),
						"y" lineTo script("anything"))))
			.assertEqualTo(true)
	}

	@Test
	fun matchesFalse() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")))
			.matches(
				script(
					"vec" lineTo script(
						"x" lineTo script("anything"),
						"y" lineTo script("zero"))))
			.assertEqualTo(false)
	}
}