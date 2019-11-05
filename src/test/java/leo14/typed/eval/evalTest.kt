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
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun field() {
		script(
			"text" fieldTo "foo",
			"native" fieldTo script())
			.eval
			.assertEqualTo("foo")
	}

	@Test
	fun accessFirst() {
		script(
			"vec" fieldTo script(
				"x" fieldTo "first",
				"y" fieldTo "second"),
			"x" fieldTo script(),
			"native" fieldTo script())
			.eval
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
			.eval
			.assertEqualTo("second")
	}

	@Test
	fun wrap() {
		script(
			"x" fieldTo "first",
			"y" fieldTo "second",
			"vec" fieldTo script(),
			"x" fieldTo script(),
			"native" fieldTo script())
			.eval
			.assertEqualTo("first")
	}
}
