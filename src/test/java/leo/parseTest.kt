package leo

import leo.base.Bit
import leo.base.assertEqualTo
import kotlin.test.Test

class ParseTest {
	@Test
	fun bitZero() {
		(bitWord fieldTo term<Nothing>(zeroWord))
			.parseBit
			.assertEqualTo(Bit.ZERO)
	}

	@Test
	fun bitOne() {
		(bitWord fieldTo term<Nothing>(oneWord))
			.parseBit
			.assertEqualTo(Bit.ONE)
	}

	@Test
	fun byte() {
		(byteWord fieldTo term(
			Bit.ZERO.reflect,
			Bit.ZERO.reflect,
			Bit.ZERO.reflect,
			Bit.ZERO.reflect,
			Bit.ONE.reflect,
			Bit.ONE.reflect,
			Bit.ZERO.reflect,
			Bit.ONE.reflect))
			.parseByte
			.assertEqualTo(13.toByte())
	}
}