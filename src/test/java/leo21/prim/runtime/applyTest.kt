package leo21.prim.runtime

import leo.base.assertEqualTo
import leo14.lambda.value.native
import leo14.lambda.value.plus
import leo14.lambda.value.switch
import leo14.lambda.value.value
import leo14.number
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.StringLengthPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringTryNumberPrim
import leo21.prim.number
import leo21.prim.prim
import kotlin.math.cos
import kotlin.math.sin
import kotlin.test.Test

class ApplyTest {
	@Test
	fun doublePlusDouble() {
		NumberPlusNumberPrim
			.apply(value(prim(10.0)).plus(value(prim(20.0))))
			.assertEqualTo(value(prim(30.0)))
	}

	@Test
	fun doubleMinusDouble() {
		NumberMinusNumberPrim
			.apply(value(prim(30.0)).plus(value(prim(20.0))))
			.assertEqualTo(value(prim(10.0)))
	}

	@Test
	fun doubleTimesDouble() {
		NumberTimesNumberPrim
			.apply(value(prim(10.0)).plus(value(prim(20.0))))
			.assertEqualTo(value(prim(200.0)))
	}

	@Test
	fun doubleSinus() {
		NumberSinusPrim
			.apply(value(prim(1)))
			.assertEqualTo(value(prim(sin(1.0))))
	}

	@Test
	fun doubleCosinus() {
		NumberCosinusPrim
			.apply(value(prim(1)))
			.assertEqualTo(value(prim(cos(1.0))))
	}

	@Test
	fun stringPlusString() {
		StringPlusStringPrim
			.apply(value(prim("Hello, ")).plus(value(prim("world!"))))
			.assertEqualTo(value(prim("Hello, world!")))
	}

	@Test
	fun stringLength() {
		// TODO: char count?
		StringLengthPrim
			.apply(value(prim("Hello, world!")))
			.assertEqualTo(value(prim("Hello, world!".lengthNumber)))
	}

	@Test
	fun stringTryNumber_success() {
		StringTryNumberPrim
			.apply(value(prim("123")))
			.switch(
				{ it },
				{ value(prim("error")) })
			.assertEqualTo(value(prim(123)))
	}

	@Test
	fun stringTryNumber_error() {
		StringTryNumberPrim
			.apply(value(prim("123a")))
			.switch(
				{ it },
				{ value(prim("error")) })
			.assertEqualTo(value(prim("error")))
	}
}