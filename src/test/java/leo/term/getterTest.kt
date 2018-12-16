package leo.term

import leo.base.assertEqualTo
import leo.oneWord
import leo.twoWord
import org.junit.Test

class GetterTest {
	@Test
	fun get() {
		term(
			oneWord apply term(1),
			twoWord apply term(2))
			.get(lhsGetter)
			.assertEqualTo(term(oneWord apply term(1)))

		term(
			oneWord apply term(1),
			twoWord apply term(2))
			.get(rhsGetter)
			.assertEqualTo(term(2))
	}
}