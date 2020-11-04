package leo21.prim.evaluate

import leo.base.assertEqualTo
import leo14.lambda.value.plus
import leo14.lambda.value.value
import leo21.prim.prim
import kotlin.test.Test

class ApplyTest {
	@Test
	fun doublePlusDouble() {
		value(prim(10.0))
			.plus(value(prim(20.0)))
			.applyDoublePlusDouble
			.assertEqualTo(value(prim(30.0)))
	}

	@Test
	fun doubleMinusDouble() {
		value(prim(30.0))
			.plus(value(prim(20.0)))
			.applyDoubleMinusDouble
			.assertEqualTo(value(prim(10.0)))
	}

	@Test
	fun doubleTimesDouble() {
		value(prim(30.0))
			.plus(value(prim(20.0)))
			.applyDoubleTimesDouble
			.assertEqualTo(value(prim(600.0)))
	}

	@Test
	fun stringPlusString() {
		value(prim("Hello, "))
			.plus(value(prim("world!")))
			.applyStringPlusString
			.assertEqualTo(value(prim("Hello, world!")))
	}
}