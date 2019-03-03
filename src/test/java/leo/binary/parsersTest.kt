package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class ParsersTest {
	@Test
	fun bit() {
		bitParser.parse(Bit.ZERO).assertEqualTo(Bit.ZERO.parser)
		bitParser.parse(Bit.ONE).assertEqualTo(Bit.ONE.parser)
		bitParser.parse(Bit.ZERO)?.parse(Bit.ZERO).assertEqualTo(null)
	}

	@Test
	fun int() {
		int5Parser.parse(0.bit)?.parse(1.bit)?.parse(0.bit)?.parse(0.bit)?.parse(1.bit)
			.assertEqualTo(int0.push(0.bit).push(1.bit).push(0.bit).push(0.bit).push(1.bit).parser)
	}
}
