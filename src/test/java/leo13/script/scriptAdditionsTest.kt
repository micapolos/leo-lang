package leo13.script

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptAdditionsTest {
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
	fun resolveBody() {
		script()
			.resolve(
				"body" lineTo script(
					"vec" lineTo script(
						"x" lineTo script("one"),
						"y" lineTo script("two"))))
			.assertEqualTo(
				script(
					"x" lineTo script("one"),
					"y" lineTo script("two")))
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
}