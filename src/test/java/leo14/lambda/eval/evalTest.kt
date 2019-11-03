package leo14.lambda.eval

import leo.base.assertEqualTo
import leo14.dsl.native
import leo14.lambda.*
import leo14.script
import org.junit.Test

class EvalTest {
	@Test
	fun helloWorld() {
		value("Hello, world!")
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun invoke() {
		fn(arg0)(value("Hello, world!"))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun pairFirst() {
		pair(value("first"), value("second"))
			.first
			.eval
			.assertEqualTo("first")
	}

	@Test
	fun pairSecond() {
		pair(value("first"), value("second"))
			.second
			.eval
			.assertEqualTo("second")
	}

	@Test
	fun evalScript() {
		script(native("Hello, world!")).eval.assertEqualTo("Hello, world!")
	}
}