package leo14.typed.eval

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.script
import kotlin.test.Test

class EvalTest {
	@Test
	fun string() {
		script("Hello, world!")
			.eval
			.assertEqualTo(script("Hello, world!"))
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
}
