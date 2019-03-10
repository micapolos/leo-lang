package leo

import leo.base.EnumBit
import leo.base.assertEqualTo
import org.junit.Test

class BitEvaluatorTest {
	@Test
	fun readBitOne() {
		emptyBitEvaluator
			.evaluate(EnumBit.ONE)
			.assertEqualTo(emptyBitEvaluator.copy(byteInt = 0x80, maskInt = 0x40))
	}

	@Test
	fun readBitZero() {
		emptyBitEvaluator
			.evaluate(EnumBit.ZERO)
			.assertEqualTo(emptyBitEvaluator.copy(byteInt = 0x00, maskInt = 0x40))
	}

//	@Test
//	fun readLetterA() {
//		emptyBitReader
//			.read(Bit.ZERO)!!
//			.read(Bit.ONE)!!
//			.read(Bit.ONE)!!
//			.read(Bit.ZERO)!!
//			.read(Bit.ZERO)!!
//			.read(Bit.ZERO)!!
//			.read(Bit.ZERO)!!
//			.read(Bit.ONE)
//			.assertEqualTo(emptyBitReader.copy(bytePreprocessor = emptyBitReader.bytePreprocessor.plus(Letter.A.char.byte)))
//	}
}