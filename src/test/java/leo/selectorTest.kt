package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class SelectorTest {
	val testTerm: Term<Int> = term(
		oneWord fieldTo 1.meta.term,
		ageWord fieldTo 42.meta.term,
		ageWord fieldTo 44.meta.term,
		numberWord fieldTo term(
			firstWord fieldTo 100.meta.term,
			lastWord fieldTo 200.meta.term))

	@Test
	fun invokeEmpty() {
		selector()
			.invoke(testTerm)
			.assertEqualTo(testTerm)
	}

	@Test
	fun invokeSinglePattern() {
		selector(oneWord.getter)
			.invoke(testTerm)
			.assertEqualTo(1.meta.term)
	}

	@Test
	fun invokeMultiplePattern() {
		selector(ageWord.getter)
			.invoke(testTerm)
			.assertEqualTo(
				term(
					theWord fieldTo 42.meta.term,
					theWord fieldTo 44.meta.term))
	}

	@Test
	fun invokeMissingPattern() {
		selector(personWord.getter)
			.invoke(testTerm)
			.assertEqualTo(null)
	}

	@Test
	fun invokeDeep() {
		selector(numberWord.getter, lastWord.getter)
			.invoke(testTerm)
			.assertEqualTo(200.meta.term)
	}

	@Test
	fun parse_argument() {
		argumentWord.term
			.parseSelector(oneWord.term)
			.assertEqualTo(selector())
	}

	@Test
	fun parse_simple() {
		term(oneWord fieldTo argumentWord.term)
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					twoWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord.getter))
	}

	@Test
	fun parse_deep() {
		term(twoWord fieldTo term(oneWord fieldTo argumentWord.term))
			.parseSelector(term(oneWord fieldTo term(twoWord fieldTo numberWord.term)))
			.assertEqualTo(selector(oneWord.getter, twoWord.getter))
	}

	@Test
	fun parse_mismatch() {
		term(oneWord fieldTo argumentWord.term)
			.parseSelector(term(twoWord fieldTo numberWord.term))
			.assertEqualTo(null)
	}

	@Test
	fun parse_multiple() {
		term(oneWord fieldTo argumentWord.term)
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord.getter))
	}

	@Test
	fun parse_multiple_last() {
		term(lastWord fieldTo term(oneWord fieldTo argumentWord.term))
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord.getter, lastGetter))
	}

	@Test
	fun parse_multiple_previous() {
		term(previousWord fieldTo term(oneWord fieldTo argumentWord.term))
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord.getter, previousGetter))
	}

	@Test
	fun parse_multiple_previous_last() {
		term(lastWord fieldTo (term(previousWord fieldTo term(oneWord fieldTo argumentWord.term))))
			.parseSelector(
				term(
					oneWord fieldTo numberWord.term,
					oneWord fieldTo stringWord.term))
			.assertEqualTo(selector(oneWord.getter, previousGetter, lastGetter))
	}

	@Test
	fun parse_multiple_previous_previous_last() {
		term(lastWord fieldTo term(previousWord fieldTo term(previousWord fieldTo term(oneWord fieldTo argumentWord.term))))
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
				oneWord fieldTo argumentWord.term))
			.parseSelectorTerm(
				term(oneWord fieldTo numberWord.term))
			.assertEqualTo(
				term(
					nameWord fieldTo selector(oneWord.getter).meta.term))
	}

	@Test
	fun bodyInvoke() {
		term(
			argumentWord fieldTo selector(itWord.getter).meta.term,
			timesWord fieldTo selector(plusWord.getter).meta.term)
			.invoke(
				term(
					itWord fieldTo 1.meta.term,
					plusWord fieldTo 2.meta.term))
			.assertEqualTo(
				term(
					argumentWord fieldTo 1.meta.term,
					timesWord fieldTo 2.meta.term))
	}
}