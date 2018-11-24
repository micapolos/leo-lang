package leo

import leo.base.Bit
import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class ScalarTest {
	@Test
	fun metaString() {
		1.meta.scalar.string.assertEqualTo(1.string)
	}

	@Test
	fun wordString() {
		oneWord.scalar.string.assertEqualTo("${oneWord.string}$beginString$endString")
	}

	@Test
	fun metaTokenStream() {
		1.meta.scalar.tokenStream.assertContains(1.meta.token)
	}

	@Test
	fun wordTokenStream() {
		oneWord.scalar.tokenStream.assertContains(oneWord.token, begin.token, end.token)
	}

	@Test
	fun metaReflect() {
		Bit.ZERO.meta.scalar
			.reflect(Bit::reflect)
			.assertEqualTo(scalarWord fieldTo term(Bit.ZERO.meta.reflect(Bit::reflect)))
	}

	@Test
	fun wordReflect() {
		oneWord.scalar
			.reflect
			.assertEqualTo(scalarWord fieldTo oneWord.reflect.term)
	}

//	@Test
//	fun metaReflectAndParse() {
//		Bit.ZERO.meta.scalar
//			.assertReflectAndParseWorks({ reflect(Bit::reflect) }, Field<Nothing>::parseScalar)
//	}

	@Test
	fun wordReflectAndParse() {
		oneWord.scalar
			.assertReflectAndParseWorks(Scalar<Nothing>::reflect, Field<Nothing>::parseScalar)
	}
}