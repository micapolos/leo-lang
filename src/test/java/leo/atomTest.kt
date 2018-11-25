package leo

import leo.base.Bit
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class AtomTest {
	@Test
	fun metaString() {
		1.meta.atom.string.assertEqualTo("$metaChar${1.string}")
	}

	@Test
	fun wordString() {
		oneWord.atom.string.assertEqualTo("${oneWord.string}$beginString$endString")
	}

	@Test
	fun metaTokenStream() {
		1.meta.atom.token.assertEqualTo(1.meta.token)
	}

	@Test
	fun wordTokenStream() {
		oneWord.atom.token.assertEqualTo(oneWord.token)
	}

	@Test
	fun metaReflect() {
		Bit.ZERO.meta.atom
			.reflect(Bit::reflect)
			.assertEqualTo(atomWord fieldTo Bit.ZERO.reflect.term)
	}

	@Test
	fun wordReflect() {
		oneWord.atom
			.reflect
			.assertEqualTo(atomWord fieldTo oneWord.term)
	}

//	@Test
//	fun metaReflectAndParse() {
//		Bit.ZERO.meta.scalar
//			.assertReflectAndParseWorks({ reflect(Bit::reflect) }, Field<Nothing>::parseScalar)
//	}

	@Test
	fun wordReflectAndParse() {
		oneWord.atom
			.assertReflectAndParseWorks(Atom<Nothing>::reflect, Field<Nothing>::parseAtom)
	}
}