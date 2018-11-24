package leo

import leo.base.Bit
import leo.base.assertEqualTo
import leo.base.string
import org.junit.Test

class MetaTest {
	@Test
	fun string() {
		1.meta.string.assertEqualTo(1.string)
	}

	@Test
	fun reflect() {
		Bit.ZERO.meta
			.reflect(Bit::reflect)
			.assertEqualTo(metaWord fieldTo term(Bit.ZERO.reflect))
	}

	@Test
	fun reflectAndParse() {
		Bit.ZERO.meta
			.assertReflectAndParseWorks({ reflect(Bit::reflect) }, { parseMeta(Field<Nothing>::parseBit) })
	}
}