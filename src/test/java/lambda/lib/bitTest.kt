package lambda.lib

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
		zeroBit.bitNegate.assertEqualTo(oneBit)
		oneBit.bitNegate.assertEqualTo(zeroBit)

		zeroBit.bitAnd(zeroBit).assertEqualTo(zeroBit)
		zeroBit.bitAnd(oneBit).assertEqualTo(zeroBit)
		oneBit.bitAnd(zeroBit).assertEqualTo(zeroBit)
		oneBit.bitAnd(oneBit).assertEqualTo(oneBit)

		zeroBit.bitOr(zeroBit).assertEqualTo(zeroBit)
		zeroBit.bitOr(oneBit).assertEqualTo(oneBit)
		oneBit.bitOr(zeroBit).assertEqualTo(oneBit)
		oneBit.bitOr(oneBit).assertEqualTo(oneBit)

		zeroBit.bitXor(zeroBit).assertEqualTo(zeroBit)
		zeroBit.bitXor(oneBit).assertEqualTo(oneBit)
		oneBit.bitXor(zeroBit).assertEqualTo(oneBit)
		oneBit.bitXor(oneBit).assertEqualTo(zeroBit)
	}
}