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
		term(1)
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(null)

		term<Nothing>(oneWord)
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(the(null))

		term(oneWord apply term(1))
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(the(term(1)))

		term(
			oneWord apply term(1),
			twoWord apply term(2))
			.matchPartial(twoWord) { the(it) }
			.assertEqualTo(the(term(2)))

		term(
			oneWord apply term(1),
			twoWord apply term(2))
			.matchPartial(oneWord) { the(it) }
			.assertEqualTo(null)
	}

	@Test
	fun match() {
		term(oneWord apply term(1))
			.match(oneWord) { the(it) }
			.assertEqualTo(the(term(1)))

		term(oneWord apply term(1))
			.match(twoWord) { it }
			.assertEqualTo(null)

		term(
			oneWord apply term(1),
			oneWord apply term(1))
			.match(oneWord) { it }
			.assertEqualTo(null)

		term(
			oneWord apply term(1),
			twoWord apply term(2))
			.match(oneWord, twoWord) { one, name -> one to name }
			.assertEqualTo(term(1) to term(2))
	}
}