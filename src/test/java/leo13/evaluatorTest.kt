package leo13

import leo.base.assertEqualTo
import org.junit.Test

class EvaluatorTest {
	@Test
	fun argument() {
		evaluator()
			.pushBinding(value(0 lineTo value()) of type("one" lineTo type()))
			.push("given" lineTo script())!!
			.script
			.assertEqualTo(script("one" lineTo script()))
	}

	@Test
	fun switch() {

	}
}