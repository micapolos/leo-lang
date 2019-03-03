package leo

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.Bit
import org.junit.Test

class MetaTest {
	@Test
	fun string() {
		1.meta.string.assertEqualTo("$metaChar${1.string}")
	}

	@Test
	fun reflect() {
		Bit.ZERO.meta
			.reflect(Bit::reflect)
			.assertEqualTo(metaWord fieldTo term(Bit.ZERO.reflect))
	}

	@Test
	fun reflectAndParse() {
//		Bit.ZERO.meta
//			.assertReflectAndParseWorks(Bit::reflect, Field<Nothing>::parseBit)
	}
}