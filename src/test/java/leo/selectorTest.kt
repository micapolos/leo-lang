package leo

import leo.base.assertEqualTo
import kotlin.test.Test


class SelectorTest {
	val testTerm: Term<Int> = term(
		oneWord fieldTo term(1),
		ageWord fieldTo term(42),
		ageWord fieldTo term(44),
		numberWord fieldTo term(
			firstWord fieldTo term(100),
			lastWord fieldTo term(200)))

	@Test
	fun invokeEmpty() {
		selector()
			.invoke(testTerm)
			.assertEqualTo(testTerm)
	}

	@Test
	fun invokeSingleChoice() {
		selector(oneWord)
			.invoke(testTerm)
			.assertEqualTo(term(1))
	}

	@Test
	fun invokeMultipleChoice() {
		selector(ageWord)
			.invoke(testTerm)
			.assertEqualTo(
				term(
					previousWord fieldTo term(
						lastWord fieldTo term(42)),
					lastWord fieldTo term(44)))
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
			.assertEqualTo(term(200))
	}

	@Test
	fun parse_this() {
		term<Value>(thisWord)
			.parseSelector(term(oneWord))
			.assertEqualTo(selector())
	}

	@Test
	fun parse_simple() {
		term<Value>(oneWord fieldTo term(thisWord))
			.parseSelector(
				term(
					oneWord fieldTo term(numberWord),
					twoWord fieldTo term(stringWord)))
			.assertEqualTo(selector(oneWord))
	}

	@Test
	fun parse_deep() {
		term<Value>(twoWord fieldTo term(oneWord fieldTo term(thisWord)))
			.parseSelector(term(oneWord fieldTo term(twoWord fieldTo term(numberWord))))
			.assertEqualTo(selector(oneWord, twoWord))
	}

	@Test
	fun parse_mismatch() {
		term<Value>(oneWord fieldTo term(thisWord))
			.parseSelector(term(twoWord fieldTo term(numberWord)))
			.assertEqualTo(null)
	}

	@Test
	fun parse_multiple() {
		term<Value>(oneWord fieldTo term(thisWord))
			.parseSelector(
				term(
					oneWord fieldTo term(numberWord),
					oneWord fieldTo term(stringWord)))
			.assertEqualTo(selector(oneWord))
	}

	@Test
	fun parse_multiple_last() {
		term<Value>(lastWord fieldTo term(oneWord fieldTo term(thisWord)))
			.parseSelector(
				term(
					oneWord fieldTo term(numberWord),
					oneWord fieldTo term(stringWord)))
			.assertEqualTo(selector(oneWord, lastWord))
	}

	@Test
	fun parse_multiple_previous() {
		term<Value>(previousWord fieldTo term(oneWord fieldTo term(thisWord)))
			.parseSelector(
				term(
					oneWord fieldTo term(numberWord),
					oneWord fieldTo term(stringWord)))
			.assertEqualTo(selector(oneWord, previousWord))
	}

	@Test
	fun parse_multiple_previous_last() {
		term<Value>(lastWord fieldTo (term(previousWord fieldTo term(oneWord fieldTo term(thisWord)))))
			.parseSelector(
				term(
					oneWord fieldTo term(numberWord),
					oneWord fieldTo term(stringWord)))
			.assertEqualTo(selector(oneWord, previousWord, lastWord))
	}

	@Test
	fun parse_multiple_previous_previous_last() {
		term<Value>(lastWord fieldTo term(previousWord fieldTo term(previousWord fieldTo term(oneWord fieldTo term(thisWord)))))
			.parseSelector(
				term(
					oneWord fieldTo term(numberWord),
					oneWord fieldTo term(stringWord)))
			.assertEqualTo(null)
	}

	@Test
	fun parse_literal() {
		term<Value>(oneWord)
			.parseSelectorTerm(term(personWord))
			.assertEqualTo(term(oneWord))
	}

	@Test
	fun parse_list() {
		term<Value>(
			nameWord fieldTo term(stringWord),
			ageWord fieldTo term(numberWord))
			.parseSelectorTerm(term(personWord))
			.assertEqualTo(
				term(
					nameWord fieldTo term(stringWord),
					ageWord fieldTo term(numberWord)))
	}

	@Test
	fun parse_selector() {
		term<Value>(
			nameWord fieldTo term(
				oneWord fieldTo term(thisWord)))
			.parseSelectorTerm(
				term(oneWord fieldTo term(numberWord)))
			.assertEqualTo(
				term(
					nameWord fieldTo term(selector(oneWord))))
	}

	@Test
	fun bodyInvoke() {
		term(
			thisWord fieldTo term(selector(itWord)),
			timesWord fieldTo term(selector(plusWord)))
			.invoke(
				term(
					itWord fieldTo term(1),
					plusWord fieldTo term(2)))
			.assertEqualTo(
				term(
					thisWord fieldTo term(1),
					timesWord fieldTo term(2)))
	}
}