package leo

import leo.base.assertEqualTo
import leo.base.goBack
import leo.base.string
import kotlin.test.Test

class PatternTest {
	@Test
	fun oneOfString() {
		oneOfPattern(oneWord.term, twoWord.term)
			.string
			.assertEqualTo("pattern(recurse back, recurse back)")
	}

	@Test
	fun recursionString() {
		recurse(goBack, goBack).pattern
			.string
			.assertEqualTo("pattern recursion(go back, go back)")
	}

	@Test
	fun parse_onePattern() {
		term(eitherWord fieldTo oneWord.term)
			.parsePattern
			.assertEqualTo(oneOfPattern(oneWord.term))
	}

	@Test
	fun parse_manyPatterns() {
		term(
			eitherWord fieldTo oneWord.term,
			eitherWord fieldTo twoWord.term)
			.parsePattern
			.assertEqualTo(
				oneOfPattern(
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
			.matches(oneOfPattern(oneWord.term, twoWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_second() {
		twoWord.term
			.matches(oneOfPattern(oneWord.term, twoWord.term))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_none() {
		ageWord.term
			.matches(oneOfPattern(oneWord.term, twoWord.term))
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
				oneOfPattern(
					stringWord.term,
					numberWord.term).meta.term)
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
					oneOfPattern(
						stringWord.term,
						numberWord.term).meta.term))
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
				oneOfPattern(
					nameWord.term,
					ageWord.term).meta.term)
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_noRecursion() {
		oneWord.term
			.matches(
				term(
					oneOfPattern(
						oneWord.term(),
						term(plusWord fieldTo term(recurse(goBack).pattern)))))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_recursionBack() {
		term(incrementWord fieldTo zeroWord.term)
			.matches(
				term(
					oneOfPattern(
						zeroWord.term(),
						term(incrementWord fieldTo term(recurse(goBack).pattern)))))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_recursionBackBack() {
		term(oneWord fieldTo term(plusWord fieldTo zeroWord.term))
			.matches(
				term(
					oneOfPattern(
						zeroWord.term(),
						term(oneWord fieldTo term(plusWord fieldTo term(recurse(goBack, goBack).pattern))))))
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_recursionBack_twoLevels() {
		term(incrementWord fieldTo term(incrementWord fieldTo zeroWord.term))
			.matches(
				term(
					oneOfPattern(
						zeroWord.term(),
						term(incrementWord fieldTo term(recurse(goBack).pattern)))))
			.assertEqualTo(true)
	}

	val treePatternTerm = term(
		treeWord fieldTo
			term(
				oneOfPattern(
					leafWord.term(),
					term(
						leftWord fieldTo term(recurse(goBack, goBack, goBack).pattern),
						rightWord fieldTo term(recurse(goBack, goBack, goBack).pattern)))))

	@Test
	fun termMatches_treeLeaf() {
		term(treeWord fieldTo leafWord.term)
			.matches(treePatternTerm)
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_treeLeftRight() {
		term(treeWord fieldTo term(
			leftWord fieldTo term(
				treeWord fieldTo leafWord.term),
			rightWord fieldTo term(
				treeWord fieldTo leafWord.term)))
			.matches(treePatternTerm)
			.assertEqualTo(true)
	}

	@Test
	fun termMatches_deepTree() {
		term(
			treeWord fieldTo term(
				leftWord fieldTo term(
					leftWord fieldTo term(
						treeWord fieldTo leafWord.term),
					rightWord fieldTo term(
						treeWord fieldTo term(
							leftWord fieldTo term(
								treeWord fieldTo leafWord.term),
							rightWord fieldTo term(
								treeWord fieldTo leafWord.term))),
					rightWord fieldTo term(
						treeWord fieldTo leafWord.term))))
			.matches(treePatternTerm)
			.assertEqualTo(true)
	}
}