package leo13

import leo.base.assertEqualTo
import org.junit.Test

class EvaluatorTest {
	@Test
	fun plain() {
		evaluator()
			.push(script("one" lineTo script()))!!
			.typedScript
			.assertEqualTo(script("one" lineTo script()) of type("one" lineTo type()))

		evaluator()
			.push(script("one" lineTo script("two" lineTo script())))!!
			.typedScript
			.assertEqualTo(
				script("one" lineTo script("two" lineTo script())) of
					type("one" lineTo type("two" lineTo type())))

		evaluator()
			.push(script("one" lineTo script(), "plus" lineTo script("two" lineTo script())))!!
			.typedScript
			.assertEqualTo(
				script("one" lineTo script(), "plus" lineTo script("two" lineTo script())) of
					type("one" lineTo type(), "plus" lineTo type("two" lineTo type())))
	}

	@Test
	fun normalize() {
		evaluator()
			.push(script("one" lineTo script(), "two" lineTo script()))!!
			.typedScript
			.assertEqualTo(
				script("two" lineTo script("one" lineTo script())) of
					type("two" lineTo type("one" lineTo type())))
	}

	@Test
	fun argument() {
		evaluator()
			.pushBinding(value(0 lineTo value()) of type("one" lineTo type()))
			.push("given" lineTo script())!!
			.typedScript
			.assertEqualTo(script("one" lineTo script()) of type("one" lineTo type()))
	}
}