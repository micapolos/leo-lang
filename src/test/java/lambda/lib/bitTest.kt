package lambda.lib

import lambda.assertEvalsTo
import lambda.invoke
import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import kotlin.test.Test

class BitTest {
	@Test
	fun equality() {
		zeroBit.assertEqualTo(zeroBit)
		zeroBit.assertNotEqualTo(oneBit)
		oneBit.assertNotEqualTo(zeroBit)
		oneBit.assertEqualTo(oneBit)
	}

	@Test
	fun math() {
		zeroBit.bitNegate.assertEvalsTo(oneBit)
		oneBit.bitNegate.assertEvalsTo(zeroBit)

		zeroBit.bitAnd(zeroBit).assertEvalsTo(zeroBit)
		zeroBit.bitAnd(oneBit).assertEvalsTo(zeroBit)
		oneBit.bitAnd(zeroBit).assertEvalsTo(zeroBit)
		oneBit.bitAnd(oneBit).assertEvalsTo(oneBit)

		zeroBit.bitOr(zeroBit).assertEvalsTo(zeroBit)
		zeroBit.bitOr(oneBit).assertEvalsTo(oneBit)
		oneBit.bitOr(zeroBit).assertEvalsTo(oneBit)
		oneBit.bitOr(oneBit).assertEvalsTo(oneBit)

		zeroBit.bitXor(zeroBit).assertEvalsTo(zeroBit)
		zeroBit.bitXor(oneBit).assertEvalsTo(oneBit)
		oneBit.bitXor(zeroBit).assertEvalsTo(oneBit)
		oneBit.bitXor(oneBit).assertEvalsTo(zeroBit)
	}
}