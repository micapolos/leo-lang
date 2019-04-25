package leo32

import leo.base.assertEqualTo
import leo.base.empty
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class BitReaderTest {
	@Test
	fun plus() {
		empty
			.bitReader
			.plus(zero.bit)
			.assertEqualTo(empty.bitReader.copy(byteAcc = 0.toByte(), byteMask = 0x40.toByte()))

		empty
			.bitReader
			.plus(zero.bit)!!
			.plus(zero.bit)!!
			.plus(one.bit)!!
			.plus(zero.bit)!!
			.plus(zero.bit)!!
			.plus(zero.bit)!!
			.plus(zero.bit)!!
			.plus(zero.bit)
			.assertEqualTo(empty.byteReader.plus(32)!!.bitReader)
	}
}