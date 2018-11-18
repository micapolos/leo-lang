package leo.lab

import leo.base.Bit
import leo.base.assertEqualTo
import kotlin.test.Test

class BitReaderTest {
	@Test
	fun plusBit() {
		BitReader(emptyBitEvaluator, null)
			.read(Bit.ONE)
			.assertEqualTo(BitReader(emptyBitEvaluator.evaluate(Bit.ONE)!!, null))
	}
}