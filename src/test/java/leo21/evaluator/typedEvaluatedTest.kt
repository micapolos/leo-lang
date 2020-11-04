package leo21.evaluator

import leo.base.assertEqualTo
import leo14.lambda.fn
import leo14.lambda.value.value
import leo21.prim.prim
import leo21.term.term
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.doubleType
import leo21.type.lineTo
import leo21.type.stringType
import leo21.type.type
import leo21.typed.ArrowTyped
import leo21.typed.case
import leo21.typed.doubleMinus
import leo21.typed.doublePlus
import leo21.typed.doubleTimes
import leo21.typed.end
import leo21.typed.get
import leo21.typed.lineTo
import leo21.typed.stringPlus
import leo21.typed.switch
import leo21.typed.typed
import kotlin.test.Test

class TypedEvaluateTest {
	@Test
	fun get_first() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("x")
			.evaluated
			.assertEqualTo(value(prim(10.0)) of type("x" lineTo doubleType))
	}

	@Test
	fun get_second() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("y")
			.evaluated
			.assertEqualTo(value(prim(20.0)) of type("y" lineTo doubleType))
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
			.evaluated
			.assertEqualTo(value(prim("false")) of stringType)
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
			.evaluated
			.assertEqualTo(value(prim("true")) of stringType)
	}

	@Test
	fun doublePlus() {
		typed(10.0)
			.doublePlus(typed(20.0))
			.evaluated
			.assertEqualTo(value(prim(30.0)) of doubleType)
	}

	@Test
	fun doubleMinus() {
		typed(30.0)
			.doubleMinus(typed(20.0))
			.evaluated
			.assertEqualTo(value(prim(10.0)) of doubleType)
	}

	@Test
	fun doubleTimes() {
		typed(10.0)
			.doubleTimes(typed(20.0))
			.evaluated
			.assertEqualTo(value(prim(200.0)) of doubleType)
	}

	@Test
	fun stringPlus() {
		typed("Hello, ")
			.stringPlus(typed("world!"))
			.evaluated
			.assertEqualTo(value(prim("Hello, world!")) of stringType)
	}
}