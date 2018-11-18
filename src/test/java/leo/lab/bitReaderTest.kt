package leo.lab

import leo.base.Bit
import leo.base.assertEqualTo
import kotlin.test.Test

class BitReaderTest {
	@Test
	fun plusBit() {
		BitReader(emptyBitEvaluator, null)
			.plus(Bit.ONE)
			.assertEqualTo(BitReader(emptyBitEvaluator.read(Bit.ONE)!!, null))
	}
}