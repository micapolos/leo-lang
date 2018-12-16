package leo.term

import leo.base.assertEqualTo
import leo.bitWord
import leo.oneWord
import leo.twoWord
import leo.zeroWord
import kotlin.test.Test

class OneOfTest {
	@Test
	fun bitIsMatching() {
		bitPattern
			.isMatching(term(bitWord apply term(zeroWord)))
			.assertEqualTo(true)

		bitPattern
			.isMatching(term(bitWord apply term(oneWord)))
			.assertEqualTo(true)

		bitPattern
			.isMatching(term(bitWord apply term(twoWord)))
			.assertEqualTo(false)
	}

	@Test
	fun naturalNumberIsMatching() {
		naturalNumberPattern
			.isMatching(naturalNumberZeroScript)
			.assertEqualTo(true)

		naturalNumberPattern
			.isMatching(naturalNumberOneScript)
			.assertEqualTo(true)

		naturalNumberPattern
			.isMatching(naturalNumberTwoScript)
			.assertEqualTo(true)
	}

	@Test
	fun unitsIsMatching() {
		unitsPattern
			.isMatching(unitScript)
			.assertEqualTo(true)

		unitsPattern
			.isMatching(twoUnitsScript)
			.assertEqualTo(true)

		unitsPattern
			.isMatching(threeUnitsScript)
			.assertEqualTo(true)
	}
}