package leo14.typed.eval

import leo.base.assertEqualTo
import leo14.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun string() {
		script(literal("Hello, world!"))
			.eval
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun field() {
		script("foo" fieldTo script())
			.eval
			.assertEqualTo(script("foo" fieldTo script()))
	}

	@Test
	fun accessFirst() {
		script(
			"vec" fieldTo script(
				"x" fieldTo script(
					"first" fieldTo script()),
				"y" fieldTo script(
					"second" fieldTo script())),
			"x" fieldTo script())
			.eval
			.assertEqualTo(script("x" fieldTo script("first" fieldTo script())))
	}

	@Test
	fun accessSecond() {
		script(
			"vec" fieldTo script(
				"x" fieldTo script(
					"first" fieldTo script()),
				"y" fieldTo script(
					"second" fieldTo script())),
			"y" fieldTo script())
			.eval
			.assertEqualTo(script("y" fieldTo script("second" fieldTo script())))
	}

	@Test
	fun wrap() {
		script(
			"x" fieldTo script(
				"first" fieldTo script()),
			"y" fieldTo script(
				"second" fieldTo script()),
			"vec" fieldTo script())
			.eval
			.assertEqualTo(
				script(
					"vec" fieldTo script(
						"x" fieldTo script(
							"first" fieldTo script()),
						"y" fieldTo script(
							"second" fieldTo script()))))
	}

	@Test
	fun let() {
		script(
			"let" lineTo script("chicken" lineTo script()),
			"give" lineTo script("egg" lineTo script()),
			"chicken" lineTo script())
			.eval
			.assertEqualTo(script("egg" lineTo script()))

	}

	@Test
	fun transitiveLet() {
		script(
			"let" lineTo script("chicken" lineTo script()),
			"give" lineTo script("egg" lineTo script()),
			"let" lineTo script("farmer" lineTo script()),
			"give" lineTo script("chicken" lineTo script()),
			"farmer" lineTo script())
			.eval
			.assertEqualTo(script("egg" lineTo script()))
	}

	@Test
	fun nonRecursiveLet() {
		script(
			"let" lineTo script("chicken" lineTo script()),
			"give" lineTo script("chicken" lineTo script()),
			"chicken" lineTo script())
			.eval
			.assertEqualTo(script("chicken" lineTo script()))
	}

	@Test
	fun saveAs() {
		script(
			line(literal("chicken")),
			line("save"),
			"as" lineTo script("foo"),
			line("foo"))
			.eval
			.assertEqualTo(script(literal("chicken")))
	}

	@Test
	fun functionGive() {
		script(
			"function" lineTo script(
				"takes" lineTo script(
					"chicken" lineTo script()),
				"gives" lineTo script(
					"egg" lineTo script())),
			"give" lineTo script(
				"chicken" lineTo script()))
			.eval
			.assertEqualTo(script("egg"))
	}
}
