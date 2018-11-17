package leo.lab

import leo.Letter
import leo.base.Bit
import leo.base.assertEqualTo
import org.junit.Test

class BitReaderTest {
	@Test
	fun readBitOne() {
		emptyBitReader
			.read(Bit.ONE)
			.assertEqualTo(emptyBitReader.copy(byteInt = 0x80, maskInt = 0x40))
	}

	@Test
	fun readBitZero() {
		emptyBitReader
			.read(Bit.ZERO)
			.assertEqualTo(emptyBitReader.copy(byteInt = 0x00, maskInt = 0x40))
	}

	@Test
	fun readLetterA() {
		emptyBitReader
			.read(Bit.ZERO)!!
			.read(Bit.ONE)!!
			.read(Bit.ONE)!!
			.read(Bit.ZERO)!!
			.read(Bit.ZERO)!!
			.read(Bit.ZERO)!!
			.read(Bit.ZERO)!!
			.read(Bit.ONE)
			.assertEqualTo(emptyBitReader.copy(byteReader = emptyBitReader.byteReader.plus(Letter.A)))
	}
}