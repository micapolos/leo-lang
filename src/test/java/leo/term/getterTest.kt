package leo.term

import leo.base.assertEqualTo
import leo.oneWord
import leo.twoWord
import org.junit.Test

class GetterTest {
	@Test
	fun get() {
		term(
			oneWord apply valueTerm(1),
			twoWord apply valueTerm(2))
			.get(lhsGetter)
			.assertEqualTo(term(oneWord apply valueTerm(1)))

		term(
			oneWord apply valueTerm(1),
			twoWord apply valueTerm(2))
			.get(rhsGetter)
			.assertEqualTo(valueTerm(2))
	}
}