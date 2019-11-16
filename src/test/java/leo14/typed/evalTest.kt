package leo14.typed

import leo.base.assertEqualTo
import leo14.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun string() {
		script(literal("Hello, world!"))
			.anyEval
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun field() {
		script("foo" fieldTo script())
			.anyEval
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
			.anyEval
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
			.anyEval
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
			.anyEval
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
			.anyEval
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
			.anyEval
			.assertEqualTo(script("egg" lineTo script()))
	}

	@Test
	fun nonRecursiveLet() {
		script(
			"let" lineTo script("chicken" lineTo script()),
			"give" lineTo script("chicken" lineTo script()),
			"chicken" lineTo script())
			.anyEval
			.assertEqualTo(script("chicken" lineTo script()))
	}

	@Test
	fun rememberAs() {
		script(
			line(literal("chicken")),
			"remember" lineTo script(),
			"as" lineTo script("foo"),
			"foo" lineTo script())
			.anyEval
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
			.anyEval
			.assertEqualTo(script("egg"))
	}

	@Test
	fun ofStatic() {
		script(
			"zero" lineTo script(),
			"of" lineTo script("zero"))
			.anyEval
			.assertEqualTo(script("zero"))
	}

	@Test
	fun ofChoice() {
		script(
			"zero" lineTo script(literal("zero")),
			"of" lineTo script(
				"choice" lineTo script(
					"zero" lineTo script("native"),
					"one" lineTo script("native"))))
			.anyEval
			.assertEqualTo(script("zero" lineTo script(literal("zero"))))
	}

	@Test
	fun scope() {
		script(
			"let" lineTo script("chicken"),
			"give" lineTo script("egg"),
			"let" lineTo script("horse"),
			"give" lineTo script("shit"),
			"scope" lineTo script())
			.anyEval
			.assertEqualTo(
				script(
					"scope" lineTo script(
						"function" lineTo script(
							"takes" lineTo script("chicken"),
							"gives" lineTo script("egg")),
						"function" lineTo script(
							"takes" lineTo script("horse"),
							"gives" lineTo script("shit")))))
	}
}
