package leo

import leo.base.EnumBit
import leo.base.assertEqualTo
import kotlin.test.Test

class BitTest {
	@Test
	fun reflect() {
		EnumBit.ZERO.reflect.assertEqualTo(bitWord fieldTo zeroWord.term)
		EnumBit.ONE.reflect.assertEqualTo(bitWord fieldTo oneWord.term)
	}

	@Test
	fun parseAndReflect() {
		EnumBit.ZERO.assertReflectAndParseWorks(EnumBit::reflect, Field<Nothing>::parseBit)
		EnumBit.ONE.assertReflectAndParseWorks(EnumBit::reflect, Field<Nothing>::parseBit)
	}
}