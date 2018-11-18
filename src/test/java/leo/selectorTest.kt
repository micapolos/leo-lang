package leo

import leo.base.assertEqualTo
import leo.base.the
import kotlin.test.Test

class SelectorTest {
	val testTerm: Term<Int> = term(
		oneWord fieldTo 1.metaTerm,
		ageWord fieldTo 42.metaTerm,
		ageWord fieldTo 44.metaTerm,
		numberWord fieldTo term(
			firstWord fieldTo 100.metaTerm,
			lastWord fieldTo 200.metaTerm))

	@Test
	fun invokeEmpty() {
		selector()
			.invoke(testTerm)
			.assertEqualTo(testTerm.the)
	}

	@Test
	fun invokeSingleChoice() {
		selector(oneWord)
			.invoke(testTerm)
			.assertEqualTo(1.metaTerm.the)
	}

	@Test
	fun invokeMultipleChoice() {
		selector(ageWord)
			.invoke(testTerm)
			.assertEqualTo(
				term(
					previousWord fieldTo term(
						lastWord fieldTo 42.metaTerm),
					lastWord fieldTo 44.metaTerm).the)
	}

	@Test
	fun invokeMissingChoice() {
		selector(personWord)
			.invoke(testTerm)
			.assertEqualTo(null)
	}

	@Test
	fun invokeDeep() {
		selector(numberWord, lastWord)
			.invoke(testTerm)
			.assertEqualTo(200.metaTerm.the)
	}

	@Test
	fun parse_this() {
		thisWord.term
			.parseSelector(oneWord.term)
			.assertEqualTo(selector())
	}

	@Test
	fun parse_simple() {
		term(oneWord fieldTo thisWord.term)
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					twoWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord))
	}

	@Test
	fun parse_deep() {
		term(twoWord fieldTo term(oneWord fieldTo thisWord.term))
			.parseSelector(term(oneWord fieldTo term(twoWord fieldTo numberWord.term)))
			.assertEqualTo(selector(oneWord, twoWord))
	}

	@Test
	fun parse_mismatch() {
		term(oneWord fieldTo thisWord.term)
			.parseSelector(term(twoWord fieldTo numberWord.term))
			.assertEqualTo(null)
	}

	@Test
	fun parse_multiple() {
		term(oneWord fieldTo thisWord.term)
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord))
	}

	@Test
	fun parse_multiple_last() {
		term(lastWord fieldTo term(oneWord fieldTo thisWord.term))
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord, lastWord))
	}

	@Test
	fun parse_multiple_previous() {
		term(previousWord fieldTo term(oneWord fieldTo thisWord.term))
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord, previousWord))
	}

	@Test
	fun parse_multiple_previous_last() {
		term(lastWord fieldTo (term(previousWord fieldTo term(oneWord fieldTo thisWord.term))))
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord, previousWord, lastWord))
	}

	@Test
	fun parse_multiple_previous_previous_last() {
		term(lastWord fieldTo term(previousWord fieldTo term(previousWord fieldTo term(oneWord fieldTo thisWord.term))))
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(null)
	}

	@Test
	fun parse_literal() {
		oneWord.term
			.parseSelectorTerm(personWord.term)
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun parse_list() {
		term(
			nameWord fieldTo stringWord.term,
			ageWord fieldTo numberWord.term)
			.parseSelectorTerm(personWord.term)
			.assertEqualTo(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term))
	}

	@Test
	fun parse_selector() {
		term(
			nameWord fieldTo term(
				oneWord fieldTo thisWord.term))
			.parseSelectorTerm(
				term(oneWord fieldTo numberWord.term))
			.assertEqualTo(
				term(
					nameWord fieldTo selector(oneWord).metaTerm))
	}

	@Test
	fun bodyInvoke() {
		term(
			thisWord fieldTo selector(itWord).metaTerm,
			timesWord fieldTo selector(plusWord).metaTerm)
			.invoke(
				term(
					itWord fieldTo 1.metaTerm,
					plusWord fieldTo 2.metaTerm))
			.assertEqualTo(
				term(
					thisWord fieldTo 1.metaTerm,
					timesWord fieldTo 2.metaTerm).the)
	}
}