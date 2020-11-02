package leo21.typed

import leo.base.assertEqualTo
import leo14.lambda.fn
import leo21.term.term
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.lineTo
import leo21.type.stringType
import leo21.type.type
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun get_first() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("x")
			.evaluate
			.assertEqualTo(typed("x" lineTo typed(10.0)))
	}

	@Test
	fun get_second() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("y")
			.evaluate
			.assertEqualTo(typed("y" lineTo typed(20.0)))
	}

	@Test
	fun switch_first() {
		typed(
			"bit" lineTo choice(
				"zero" lineTo type(),
				"one" lineTo type())
				.typed("zero" lineTo typed()))
			.switch
			.case("zero", ArrowTyped(fn(term("false")), type("zero" lineTo type()) arrowTo stringType))
			.case("one", ArrowTyped(fn(term("true")), type("one" lineTo type()) arrowTo stringType))
			.end
			.evaluate
			.assertEqualTo(typed("false"))
	}

	@Test
	fun switch_second() {
		typed(
			"bit" lineTo choice(
				"zero" lineTo type(),
				"one" lineTo type())
				.typed("one" lineTo typed()))
			.switch
			.case("zero", ArrowTyped(fn(term("false")), type("zero" lineTo type()) arrowTo stringType))
			.case("one", ArrowTyped(fn(term("true")), type("one" lineTo type()) arrowTo stringType))
			.end
			.evaluate
			.assertEqualTo(typed("true"))
	}

	@Test
	fun doublePlus() {
		typed(10.0)
			.doublePlus(typed(20.0))
			.evaluate
			.assertEqualTo(typed(30.0))
	}

	@Test
	fun doubleMinus() {
		typed(30.0)
			.doubleMinus(typed(20.0))
			.evaluate
			.assertEqualTo(typed(10.0))
	}

	@Test
	fun doubleTimes() {
		typed(10.0)
			.doubleTimes(typed(20.0))
			.evaluate
			.assertEqualTo(typed(200.0))
	}

	@Test
	fun stringPlus() {
		typed("Hello, ")
			.stringPlus(typed("world!"))
			.evaluate
			.assertEqualTo(typed("Hello, world!"))
	}
}