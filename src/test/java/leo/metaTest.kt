package leo

import leo.base.EnumBit
import leo.base.assertEqualTo
import leo.base.string
import org.junit.Test

class MetaTest {
	@Test
	fun string() {
		1.meta.string.assertEqualTo("$metaChar${1.string}")
	}

	@Test
	fun reflect() {
		EnumBit.ZERO.meta
			.reflect(EnumBit::reflect)
			.assertEqualTo(metaWord fieldTo term(EnumBit.ZERO.reflect))
	}

	@Test
	fun reflectAndParse() {
//		Bit.ZERO.meta
//			.assertReflectAndParseWorks(Bit::reflect, Field<Nothing>::parseBit)
	}
}