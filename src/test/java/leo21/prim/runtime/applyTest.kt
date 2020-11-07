package leo21.prim.runtime

import leo.base.assertEqualTo
import leo14.lambda.value.plus
import leo14.lambda.value.value
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.prim
import kotlin.test.Test

class ApplyTest {
	@Test
	fun doublePlusDouble() {
		DoublePlusDoublePrim
			.apply(value(prim(10.0)).plus(value(prim(20.0))))
			.assertEqualTo(value(prim(30.0)))
	}

	@Test
	fun doubleMinusDouble() {
		DoubleMinusDoublePrim
			.apply(value(prim(30.0)).plus(value(prim(20.0))))
			.assertEqualTo(value(prim(10.0)))
	}

	@Test
	fun doubleTimesDouble() {
		DoubleTimesDoublePrim
			.apply(value(prim(10.0)).plus(value(prim(20.0))))
			.assertEqualTo(value(prim(200.0)))
	}

	@Test
	fun stringPlusString() {
		StringPlusStringPrim
			.apply(value(prim("Hello, ")).plus(value(prim("world!"))))
			.assertEqualTo(value(prim("Hello, world!")))
	}
}