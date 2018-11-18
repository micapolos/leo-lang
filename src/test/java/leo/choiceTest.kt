package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class ChoiceTest {
	@Test
	fun string() {
		choice(oneWord.term, twoWord.term)
			.string
			.assertEqualTo("choice(term field(word one, term null), term field(word two, term null))")
	}

	@Test
	fun parse_onePattern() {
		term(eitherWord fieldTo oneWord.term)
			.parseChoice
			.assertEqualTo(choice(oneWord.term))
	}

	@Test
	fun parse_manyPatterns() {
		term(
			eitherWord fieldTo oneWord.term,
			eitherWord fieldTo twoWord.term)
			.parseChoice
			.assertEqualTo(
				choice(
					oneWord.term,
					twoWord.term))
	}

	@Test
	fun parse_illegal() {
		term(
			eitherWord fieldTo oneWord.term,
			personWord fieldTo nameWord.term,
			eitherWord fieldTo twoWord.term)
			.parseChoice
			.assertEqualTo(null)
	}

	@Test
	fun termMatches_first() {
		oneWord.term
			.matches(choice(oneWord.term, twoWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_second() {
		twoWord.term
			.matches(choice(oneWord.term, twoWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_none() {
		ageWord.term
			.matches(choice(oneWord.term, twoWord.term))
			.assertEqualTo(false)
	}

	@Test
	fun parse_literal() {
		oneWord.term
			.parseChoiceTerm
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun parse_list() {
		term(
			nameWord fieldTo stringWord.term,
			ageWord fieldTo numberWord.term)
			.parseChoiceTerm
			.assertEqualTo(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term))
	}

	@Test
	fun parse_oneOf() {
		term(
			eitherWord fieldTo stringWord.term,
			eitherWord fieldTo numberWord.term)
			.parseChoiceTerm
			.assertEqualTo(
				choice(
					stringWord.term,
					numberWord.term).metaTerm)
	}

	@Test
	fun parse_innerOneOf() {
		term(
			oneWord fieldTo term(
				eitherWord fieldTo stringWord.term,
				eitherWord fieldTo numberWord.term))
			.parseChoiceTerm
			.assertEqualTo(
				term(oneWord fieldTo
					choice(
						stringWord.term,
						numberWord.term).metaTerm))
	}

	@Test
	fun termMatches_literal() {
		oneWord.term
			.matches(oneWord.term)
			.assertEqualTo(true)
	}

	@Test
	fun fieldMatches_field() {
		oneWord.field
			.matches(oneWord.field)
			.assertEqualTo(true)
	}

	@Test
	fun fieldValueMatches_field() {
		oneWord.fieldTo(twoWord.term)
			.matches(oneWord.field)
			.assertEqualTo(false)
	}

	@Test
	fun fieldMatches_fieldValue() {
		oneWord.field
			.matches(oneWord.fieldTo(twoWord.term))
			.assertEqualTo(false)
	}

	@Test
	fun fieldValueMatches_fieldValue() {
		oneWord.fieldTo(twoWord.term)
			.matches(oneWord.fieldTo(twoWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_list() {
		term(
			nameWord fieldTo stringWord.term,
			ageWord fieldTo numberWord.term)
			.matches(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_oneOf_match() {
		nameWord.term
			.matches(
				choice(
					nameWord.term,
					ageWord.term).metaTerm)
			.assertEqualTo(true)
	}
}