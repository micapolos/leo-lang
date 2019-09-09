package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class AtomTest {
	@Test
	fun conversion() {
		val pattern = pattern(
			bitWord lineTo pattern(
				choice(
					either(zeroWord),
					either(oneWord))))

		val sentenceZero = sentence(bitWord lineTo sentence(zeroWord))
		val atomZero = pattern.atom(sentenceZero)
		pattern.sentence(atomZero).assertEqualTo(sentenceZero)

		val sentenceOne = sentence(bitWord lineTo sentence(oneWord))
		val atomOne = pattern.atom(sentenceOne)
		pattern.sentence(atomOne).assertEqualTo(sentenceOne)
	}
}
