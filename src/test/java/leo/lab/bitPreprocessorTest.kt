package leo.lab

import leo.base.Bit
import leo.base.assertEqualTo
import kotlin.test.Test

class BitPreprocessorTest {
	@Test
	fun plusBit() {
		BitPreprocessor(emptyBitReader, null)
			.plus(Bit.ONE)
			.assertEqualTo(BitPreprocessor(emptyBitReader.read(Bit.ONE)!!, null))
	}
}