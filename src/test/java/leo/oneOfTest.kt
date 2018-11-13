package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class PatternTest {
	@Test
	fun string() {
		oneOf(term(oneWord), term(twoWord))
			.string
			.assertEqualTo("one of(term identifier word one, term identifier word two)")
	}

	@Test
	fun parse_onePattern() {
		term<Value>(eitherWord fieldTo term(oneWord))
			.parsePattern
			.assertEqualTo(
				oneOf(
					term(oneWord)))
	}

	@Test
	fun parse_manyPatterns() {
		term<Value>(
			eitherWord fieldTo term(oneWord),
			eitherWord fieldTo term(twoWord))
			.parsePattern
			.assertEqualTo(
				oneOf(
					term(oneWord),
					term(twoWord)))
	}

	@Test
	fun parse_illegal() {
		term<Value>(
			eitherWord fieldTo term(oneWord),
			personWord fieldTo term(nameWord),
			eitherWord fieldTo term(twoWord))
			.parsePattern
			.assertEqualTo(null)
	}

	@Test
	fun termMatches_first() {
		term<Value>(oneWord)
			.matches(oneOf(term(oneWord), term(twoWord)))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_second() {
		term<Value>(twoWord)
			.matches(oneOf(term(oneWord), term(twoWord)))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_none() {
		term<Value>(ageWord)
			.matches(oneOf(term(oneWord), term(twoWord)))
			.assertEqualTo(false)
	}

	@Test
	fun parse_literal() {
		term<Value>(oneWord)
			.parsePatternTerm
			.assertEqualTo(term(oneWord))
	}

	@Test
	fun parse_list() {
		term<Value>(
			nameWord fieldTo term(stringWord),
			ageWord fieldTo term(numberWord))
			.parsePatternTerm
			.assertEqualTo(
				term(
					nameWord fieldTo term(stringWord),
					ageWord fieldTo term(numberWord)))
	}

	@Test
	fun parse_oneOf() {
		term<Value>(
			eitherWord fieldTo term(stringWord),
			eitherWord fieldTo term(numberWord))
			.parsePatternTerm
			.assertEqualTo(
				term(
					oneOf(
						term(stringWord),
						term(numberWord))))
	}

	@Test
	fun parse_innerOneOf() {
		term<Value>(
			oneWord fieldTo term(
				eitherWord fieldTo term(stringWord),
				eitherWord fieldTo term(numberWord)))
			.parsePatternTerm
			.assertEqualTo(
				term(
					oneWord fieldTo term(
						oneOf(
							term(stringWord),
							term(numberWord)))))
	}

	@Test
	fun termMatches_literal() {
		term<Value>(oneWord)
			.matches(term(oneWord))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_list() {
		term<Value>(
			nameWord fieldTo term(stringWord),
			ageWord fieldTo term(numberWord))
			.matches(
				term(
					nameWord fieldTo term(stringWord),
					ageWord fieldTo term(numberWord)))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_oneOf_match() {
		term<Value>(nameWord)
			.matches(
				term(
					oneOf(
						term(nameWord),
						term(ageWord))))
			.assertEqualTo(true)
	}
}