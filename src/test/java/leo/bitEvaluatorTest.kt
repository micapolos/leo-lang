package leo

import leo.base.assertEqualTo
import leo.binary.Bit
import org.junit.Test

class BitEvaluatorTest {
	@Test
	fun readBitOne() {
		emptyBitEvaluator
			.evaluate(Bit.ONE)
			.assertEqualTo(emptyBitEvaluator.copy(byteInt = 0x80, maskInt = 0x40))
	}

	@Test
	fun readBitZero() {
		emptyBitEvaluator
			.evaluate(Bit.ZERO)
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