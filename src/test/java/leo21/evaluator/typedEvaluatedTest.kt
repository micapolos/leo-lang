package leo21.evaluator

import leo.base.assertEqualTo
import leo14.lambda.fn
import leo14.lambda.value.value
import leo21.compiled.case
import leo21.compiled.compiled
import leo21.compiled.end
import leo21.compiled.get
import leo21.compiled.lineTo
import leo21.compiled.of
import leo21.compiled.switch
import leo21.prim.prim
import leo21.term.term
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.numberType
import leo21.type.lineTo
import leo21.type.stringType
import leo21.type.type
import kotlin.test.Test

class TypedEvaluateTest {
	@Test
	fun get_first() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)))
			.get("x")
			.evaluated
			.assertEqualTo(value(prim(10.0)) of type("x" lineTo numberType))
	}

	@Test
	fun get_second() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)))
			.get("y")
			.evaluated
			.assertEqualTo(value(prim(20.0)) of type("y" lineTo numberType))
	}

	@Test
	fun switch_first() {
		compiled(
			"bit" lineTo choice(
				"zero" lineTo type(),
				"one" lineTo type())
				.compiled("zero" lineTo compiled()))
			.switch
			.case("zero", fn(term("false")) of (type("zero" lineTo type()) arrowTo stringType))
			.case("one", fn(term("true")) of (type("one" lineTo type()) arrowTo stringType))
			.end
			.evaluated
			.assertEqualTo(value(prim("false")) of stringType)
	}

	@Test
	fun switch_second() {
		compiled(
			"bit" lineTo choice(
				"zero" lineTo type(),
				"one" lineTo type())
				.compiled("one" lineTo compiled()))
			.switch
			.case("zero", fn(term("false")) of (type("zero" lineTo type()) arrowTo stringType))
			.case("one", fn(term("true")) of (type("one" lineTo type()) arrowTo stringType))
			.end
			.evaluated
			.assertEqualTo(value(prim("true")) of stringType)
	}
}