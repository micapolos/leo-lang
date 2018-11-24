package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class PatternTest {
	@Test
	fun string() {
		pattern(oneWord.term, twoWord.term)
			.string
			.assertEqualTo("pattern(term one, term two)")
	}

	@Test
	fun parse_onePattern() {
		term(eitherWord fieldTo oneWord.term)
			.parsePattern
			.assertEqualTo(pattern(oneWord.term))
	}

	@Test
	fun parse_manyPatterns() {
		term(
			eitherWord fieldTo oneWord.term,
			eitherWord fieldTo twoWord.term)
			.parsePattern
			.assertEqualTo(
				pattern(
					oneWord.term,
					twoWord.term))
	}

	@Test
	fun parse_illegal() {
		term(
			eitherWord fieldTo oneWord.term,
			personWord fieldTo nameWord.term,
			eitherWord fieldTo twoWord.term)
			.parsePattern
			.assertEqualTo(null)
	}

	@Test
	fun termMatches_first() {
		oneWord.term
			.matches(pattern(oneWord.term, twoWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_second() {
		twoWord.term
			.matches(pattern(oneWord.term, twoWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_none() {
		ageWord.term
			.matches(pattern(oneWord.term, twoWord.term))
			.assertEqualTo(false)
	}

	@Test
	fun parse_literal() {
		oneWord.term
			.parsePatternTerm
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun parse_list() {
		term(
			nameWord fieldTo stringWord.term,
			ageWord fieldTo numberWord.term)
			.parsePatternTerm
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
			.parsePatternTerm
			.assertEqualTo(
				pattern(
					stringWord.term,
					numberWord.term).metaTerm)
	}

	@Test
	fun parse_innerOneOf() {
		term(
			oneWord fieldTo term(
				eitherWord fieldTo stringWord.term,
				eitherWord fieldTo numberWord.term))
			.parsePatternTerm
			.assertEqualTo(
				term(oneWord fieldTo
					pattern(
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
				pattern(
					nameWord.term,
					ageWord.term).metaTerm)
			.assertEqualTo(true)
	}
}