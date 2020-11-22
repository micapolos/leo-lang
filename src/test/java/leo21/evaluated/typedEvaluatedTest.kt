package leo21.evaluated

import leo.base.assertEqualTo
import leo14.lambda.value.value
import leo21.compiled.caseTo
import leo21.compiled.compiled
import leo21.compiled.get
import leo21.compiled.lineTo
import leo21.compiled.switch
import leo21.prim.prim
import leo21.type.choice
import leo21.type.lineTo
import leo21.type.numberType
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
			.switch(
				"zero" caseTo { compiled("false") },
				"one" caseTo { compiled("true") })
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
			.switch(
				"zero" caseTo { compiled("false") },
				"one" caseTo { compiled("true") })
			.evaluated
			.assertEqualTo(value(prim("true")) of stringType)
	}
}