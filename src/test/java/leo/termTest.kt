package leo

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.string
import leo.base.the
import kotlin.test.Test

class TermTest {
	val testTerm = term(
		negateWord fieldTo oneWord.term,
		plusWord fieldTo term(
			ageWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term))

	@Test
	fun string() {
		testTerm
			.string
			.assertEqualTo("negate one, plus(age number, name string)")
	}

	@Test
	fun coreString() {
		testTerm
			.coreString
			.assertEqualTo("negate(one())plus(age(number())name(string()))")
	}

	@Test
	fun isSimple_singleWord() {
		oneWord.term<Nothing>().isSimple.assertEqualTo(true)
	}

	@Test
	fun isSimple_wordChain() {
		oneWord.fieldTo(twoWord.term).term.isSimple.assertEqualTo(true)
	}

	@Test
	fun reflectScalar() {
		oneWord.term.reflect
			.assertEqualTo(termWord fieldTo oneWord.term)
	}

	@Test
	fun reflectFields() {
		term(
			ageWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term)
			.reflect
			.assertEqualTo(
				termWord fieldTo term(
					ageWord fieldTo numberWord.term,
					nameWord fieldTo stringWord.term))
	}

	@Test
	fun reflectAndParseWord() {
		oneWord.term
			.assertReflectAndParseWorks(Term<Nothing>::reflect, Field<Nothing>::parseTerm)
	}

	@Test
	fun reflectAndParseFields() {
		term(
			ageWord fieldTo numberWord.term,
			nameWord fieldTo leo.stringWord.term)
			.assertReflectAndParseWorks(Term<Nothing>::reflect, Field<Nothing>::parseTerm)
	}

	val termForGet = term(
		oneWord fieldTo 1.meta.term,
		ageWord fieldTo 42.meta.term,
		ageWord fieldTo 43.meta.term,
		twoWord fieldTo 2.meta.term)

	@Test
	fun only() {
		termForGet.onlyValueOrNull(oneWord).assertEqualTo(1.meta.term)
		termForGet.onlyValueOrNull(twoWord).assertEqualTo(2.meta.term)
		termForGet.onlyValueOrNull(ageWord).assertEqualTo(null)
		termForGet.onlyValueOrNull(nameWord).assertEqualTo(null)
	}

	@Test
	fun all() {
		termForGet.valueStreamOrNull(oneWord).assertContains(1.meta.term)
		termForGet.valueStreamOrNull(twoWord).assertContains(2.meta.term)
		termForGet.valueStreamOrNull(ageWord).assertContains(42.meta.term, 43.meta.term)
		termForGet.valueStreamOrNull(nameWord).assertContains()
	}

	@Test
	fun metaPushField() {
		1.meta.term
			.push(twoWord fieldTo oneWord.term)
			.assertEqualTo(null)
	}

	@Test
	fun wordPushField() {
		oneWord.term
			.push(twoWord fieldTo 2.meta.term)
			.assertEqualTo(null)
	}

	@Test
	fun selectSingle() {
		termForGet
			.select(oneWord)
			.assertEqualTo(1.meta.term)
	}

	@Test
	fun selectMultiple() {
		termForGet
			.select(ageWord)
			.assertEqualTo(
				term(
					theWord fieldTo 42.meta.term,
					theWord fieldTo 43.meta.term))
	}

	@Test
	fun selectMissing() {
		termForGet
			.select(personWord)
			.assertEqualTo(null)
	}

	@Test
	fun match1_wordTerm() {
		oneWord.term
			.matchFieldKey(oneWord) { the }
			.assertEqualTo(null)
	}

	@Test
	fun matchFieldKey() {
		term(oneWord fieldTo twoWord.term)
			.matchFieldKey(oneWord) { the }
			.assertEqualTo(twoWord.term.the)
	}

	@Test
	fun match1_mismatch() {
		term(oneWord fieldTo twoWord.term)
			.matchFieldKey(twoWord) { the }
			.assertEqualTo(null)
	}

	@Test
	fun match2_fieldsTerm() {
		term(
			oneWord fieldTo twoWord.term,
			twoWord fieldTo ageWord.term)
			.matchFieldKeys(oneWord, twoWord) { one, two -> one to two }
			.assertEqualTo(twoWord.term to ageWord.term)
	}

	@Test
	fun match2_mismatch() {
		term(
			oneWord fieldTo twoWord.term,
			twoWord fieldTo ageWord.term)
			.matchFieldKeys(twoWord, oneWord) { one, two -> one to two }
			.assertEqualTo(null)
	}

	@Test
	fun onlyFieldOrNull() {
		1.meta.term.onlyFieldOrNull.assertEqualTo(null)
		oneWord.term.onlyFieldOrNull.assertEqualTo(null)
		oneWord.fieldTo(twoWord.term).term.onlyFieldOrNull.assertEqualTo(oneWord fieldTo twoWord.term)
		term(
			oneWord.itField,
			twoWord.itField).onlyFieldOrNull.assertEqualTo(null)
	}

	@Test
	fun select_match() {
		term(
			nameWord fieldTo term(
				nameWord fieldTo stringWord.term,
				ageWord fieldTo numberWord.term))
			.evaluateSelect
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun select_mismatch() {
		term(
			oneWord fieldTo term(
				nameWord fieldTo stringWord.term,
				ageWord fieldTo numberWord.term))
			.evaluateSelect
			.assertEqualTo(
				term(
					oneWord fieldTo term(
						nameWord fieldTo stringWord.term,
						ageWord fieldTo numberWord.term)))
	}

	@Test
	fun select_notASelect() {
		term(
			nameWord fieldTo stringWord.term,
			ageWord fieldTo numberWord.term)
			.evaluateSelect
			.assertEqualTo(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term))
	}

	@Test
	fun tokenStream() {
		term(
			negateWord fieldTo oneWord.term,
			plusWord fieldTo term(
				ageWord fieldTo numberWord.term,
				nameWord fieldTo stringWord.term))
			.tokenStream
			.assertContains(
				negateWord.token,
				begin.control.token,
				oneWord.token,
				begin.control.token,
				end.control.token,
				end.control.token,
				plusWord.token,
				begin.control.token,
				ageWord.token,
				begin.control.token,
				numberWord.token,
				begin.control.token,
				end.control.token,
				end.control.token,
				nameWord.token,
				begin.control.token,
				stringWord.token,
				begin.control.token,
				end.control.token,
				end.control.token,
				end.control.token)
	}
}