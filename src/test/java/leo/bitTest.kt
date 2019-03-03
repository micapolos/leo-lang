package leo

import leo.base.assertEqualTo
import leo.binary.Bit
import kotlin.test.Test

class BitTest {
	@Test
	fun reflect() {
		Bit.ZERO.reflect.assertEqualTo(bitWord fieldTo zeroWord.term)
		Bit.ONE.reflect.assertEqualTo(bitWord fieldTo oneWord.term)
	}

	@Test
	fun parseAndReflect() {
		Bit.ZERO.assertReflectAndParseWorks(Bit::reflect, Field<Nothing>::parseBit)
		Bit.ONE.assertReflectAndParseWorks(Bit::reflect, Field<Nothing>::parseBit)
	}
}