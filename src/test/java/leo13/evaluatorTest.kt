package leo13

import leo.base.assertEqualTo
import org.junit.Test

class EvaluatorTest {
	@Test
	fun name() {
		evaluator()
			.push(script("one" lineTo script()))!!
			.typedScript
			.assertEqualTo(script("one" lineTo script()) of type("one" lineTo type()))
	}

	@Test
	fun line() {
		evaluator()
			.push(script("one" lineTo script("two" lineTo script())))!!
			.typedScript
			.assertEqualTo(
				script("one" lineTo script("two" lineTo script())) of
					type("one" lineTo type("two" lineTo type())))
	}

	@Test
	fun link() {
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

	@Test
	fun gives() {
		evaluator()
			.push(
				script(
					"one" lineTo script(),
					"gives" lineTo script(
						"two" lineTo script())))!!
			.typedScript
			.assertEqualTo(script() of type())
	}

	@Test
	fun typeResolution() {
		evaluator()
			.pushType(type("bit" lineTo type(choice("zero" lineTo type(), "one" lineTo type()))))
			.push("bit" lineTo script("zero" lineTo script()))!!
			.typedScript
			.assertEqualTo(
				script("bit" lineTo script("zero" lineTo script())) of
					type("bit" lineTo type(choice("zero" lineTo type(), "one" lineTo type()))))
	}
}