package leo14.typed.eval

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.lambda.eval.eval
import leo14.lambda.term
import leo14.script
import leo14.typed.typedPlus
import kotlin.test.Test

class EvalTest {
	@Test
	fun string() {
		script("Hello, world!")
			.evalAny
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun accessNative() {
		script(
			"text" fieldTo "foo",
			"native" fieldTo script())
			.evalAny
			.assertEqualTo("foo")
	}

	@Test
	fun field() {
		script("x" fieldTo "first")
			.evalAny
			.assertEqualTo("first")
	}

	@Test
	fun struct() {
		script(
			"vec" fieldTo script(
				"x" fieldTo "first",
				"y" fieldTo "second"))
			.evalAny
			.assertEqualTo(term("first").typedPlus(term("second")).eval)
	}

	@Test
	fun accessFirst() {
		script(
			"vec" fieldTo script(
				"x" fieldTo "first",
				"y" fieldTo "second"),
			"x" fieldTo script(),
			"native" fieldTo script())
			.evalAny
			.assertEqualTo("first")
	}

	@Test
	fun accessSecond() {
		script(
			"vec" fieldTo script(
				"x" fieldTo "first",
				"y" fieldTo "second"),
			"y" fieldTo script(),
			"native" fieldTo script())
			.evalAny
			.assertEqualTo("second")
	}
}