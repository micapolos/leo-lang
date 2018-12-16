package leo.term

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import leo.base.the
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		term<Nothing>(
			personWord apply term(
				firstWord apply term(nameWord apply term(stringWord)),
				lastWord apply term(nameWord apply term(stringWord)),
				ageWord apply term(numberWord)))
			.string
			.assertEqualTo("person(first name string, last name string, age number)")
	}

	@Test
	fun matchPartial() {
		valueTerm(1)
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(null)

		term<Nothing>(oneWord)
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(the(null))

		term(oneWord apply valueTerm(1))
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(the(valueTerm(1)))

		term(
			oneWord apply valueTerm(1),
			twoWord apply valueTerm(2))
			.matchPartial(twoWord) { the(it) }
			.assertEqualTo(the(valueTerm(2)))

		term(
			oneWord apply valueTerm(1),
			twoWord apply valueTerm(2))
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(null)
	}

	@Test
	fun match() {
		term(oneWord apply valueTerm(1))
			.isMatching(oneWord) { the(it) }
			.assertEqualTo(the(valueTerm(1)))

		term(oneWord apply valueTerm(1))
			.isMatching(twoWord) { it }
			.assertEqualTo(null)

		term(
			oneWord apply valueTerm(1),
			oneWord apply valueTerm(1))
			.isMatching(oneWord) { it }
			.assertEqualTo(null)

		term(
			oneWord apply valueTerm(1),
			twoWord apply valueTerm(2))
			.isMatching(oneWord, twoWord) { one, name -> one to name }
			.assertEqualTo(valueTerm(1) to valueTerm(2))
	}
}