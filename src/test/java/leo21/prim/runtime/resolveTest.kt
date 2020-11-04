package leo21.prim.runtime

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
			.resolveDoublePlusDouble
			.assertEqualTo(value(prim(30.0)))
	}

	@Test
	fun doubleMinusDouble() {
		value(prim(30.0))
			.plus(value(prim(20.0)))
			.resolveDoubleMinusDouble
			.assertEqualTo(value(prim(10.0)))
	}

	@Test
	fun doubleTimesDouble() {
		value(prim(30.0))
			.plus(value(prim(20.0)))
			.resolveDoubleTimesDouble
			.assertEqualTo(value(prim(600.0)))
	}

	@Test
	fun stringPlusString() {
		value(prim("Hello, "))
			.plus(value(prim("world!")))
			.resolveStringPlusString
			.assertEqualTo(value(prim("Hello, world!")))
	}
}