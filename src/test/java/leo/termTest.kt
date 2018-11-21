package leo

import leo.base.*
import kotlin.test.Test

class TermTest {
	val testTerm = term(
		oneWord.field,
		negateWord.field,
		plusWord fieldTo twoWord.term,
		plusWord fieldTo term(
			ageWord.field,
			personWord.field))

	@Test
	fun string() {
		testTerm
			.string
			.assertEqualTo("one, negate, plus two, plus(age, person)")
	}

	@Test
	fun coreString() {
		testTerm
			.coreString
			.assertEqualTo("one()negate()plus(two())plus(age()person())")
	}

	@Test
	fun isSimple_singleWord() {
		oneWord.term<Nothing>().isSimple.assertEqualTo(true)
	}

	@Test
	fun isSimple_wordChain() {
		oneWord.fieldTo(twoWord.term<Nothing>()).term.isSimple.assertEqualTo(true)
	}

	@Test
	fun isSimple_nodeChain() {
		term<Nothing>(oneWord.field(), twoWord.field()).isSimple.assertEqualTo(false)
	}

	@Test
	fun reflect() {
		testTerm.reflect.string.assertEqualTo("term(" +
			"field(word one, term null), " +
			"field(word negate, term null), " +
			"field(word plus, term field(word two, term null)), " +
			"field(word plus, term(field(word age, term null), " +
			"field(word person, term null))))")
	}

	val termForGet = term(
		oneWord fieldTo 1.metaTerm,
		ageWord fieldTo 42.metaTerm,
		ageWord fieldTo 43.metaTerm,
		twoWord fieldTo 2.metaTerm)

	@Test
	fun only() {
		termForGet.only(oneWord).assertEqualTo(1.metaTerm.the)
		termForGet.only(twoWord).assertEqualTo(2.metaTerm.the)
		termForGet.only(ageWord).assertEqualTo(null)
		termForGet.only(nameWord).assertEqualTo(null)
	}

	@Test
	fun all() {
		termForGet.all(oneWord).assertEqualTo(stack(1.metaTerm))
		termForGet.all(twoWord).assertEqualTo(stack(2.metaTerm))
		termForGet.all(ageWord).assertEqualTo(stack(42.metaTerm, 43.metaTerm))
		termForGet.all(nameWord).assertEqualTo(null)
	}

	@Test
	fun nullPushIdentifier() {
		nullOf<Term<Nothing>>()
			.push(oneWord)
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun nativePushIdentifier() {
		1.metaTerm
			.push(oneWord)
			.assertEqualTo(Term.Structure(1.metaTerm, oneWord, null))
	}

	@Test
	fun idPushId() {
		oneWord.term
			.push(twoWord)
			.assertEqualTo(term(oneWord.field, twoWord.field))
	}

	@Test
	fun listPushWord() {
		term(oneWord fieldTo 1.metaTerm)
			.push(twoWord)
			.assertEqualTo(term(oneWord fieldTo 1.metaTerm, twoWord.field))
	}

	@Test
	fun nullPushField() {
		nullOf<Term<Nothing>>()
			.push(oneWord fieldTo numberWord.term)
			.assertEqualTo(term(oneWord fieldTo numberWord.term))
	}

	@Test
	fun nativePushField() {
		1.metaTerm
			.push(twoWord fieldTo oneWord.term)
			.assertEqualTo(Term.Structure(1.metaTerm, twoWord, oneWord.term))
	}

	@Test
	fun wordPushField() {
		oneWord.term
			.push(twoWord fieldTo 2.metaTerm)
			.assertEqualTo(term(oneWord.field, twoWord fieldTo 2.metaTerm))
	}

	@Test
	fun listPushField() {
		term(oneWord fieldTo 1.metaTerm)
			.push(twoWord fieldTo 2.metaTerm)
			.assertEqualTo(
				term(
					oneWord fieldTo 1.metaTerm,
					twoWord fieldTo 2.metaTerm))
	}

	@Test
	fun selectSingle() {
		termForGet
			.select(oneWord)
			.assertEqualTo(1.metaTerm.the)
	}

	@Test
	fun selectMultiple() {
		termForGet
			.select(ageWord)
			.assertEqualTo(
				term(
					previousWord fieldTo term(
						lastWord fieldTo 42.metaTerm),
					lastWord fieldTo 43.metaTerm).the)
	}

	@Test
	fun selectMissing() {
		termForGet
			.select(personWord)
			.assertEqualTo(null)
	}

	@Test
	fun match1_nullTerm() {
		oneWord.term
			.match(oneWord) { it.the }
			.assertEqualTo(null.the)
	}

	@Test
	fun match1_nonNullTerm() {
		term(oneWord fieldTo twoWord.term)
			.match(oneWord) { it.the }
			.assertEqualTo(twoWord.term.the)
	}

	@Test
	fun match1_mismatch() {
		term(oneWord fieldTo twoWord.term)
			.match(twoWord) { it.the }
			.assertEqualTo(null)
	}

	@Test
	fun match2_nullTerms() {
		term(oneWord.field, twoWord.field)
			.match(oneWord, twoWord) { one, two -> one to two }
			.assertEqualTo(null to null)
	}

	@Test
	fun match2_nonNullTerm() {
		term(
			oneWord fieldTo twoWord.term,
			twoWord fieldTo ageWord.term)
			.match(oneWord, twoWord) { one, two -> one to two }
			.assertEqualTo(twoWord.term to ageWord.term)
	}

	@Test
	fun match2_mismatch() {
		term(
			oneWord fieldTo twoWord.term,
			twoWord fieldTo ageWord.term)
			.match(twoWord, oneWord) { one, two -> one to two }
			.assertEqualTo(null)
	}

	@Test
	fun onlyFieldOrNull() {
		1.metaTerm.onlyFieldOrNull.assertEqualTo(null)
		oneWord.term.onlyFieldOrNull.assertEqualTo(oneWord.field)
		oneWord.fieldTo(twoWord.term).term.onlyFieldOrNull.assertEqualTo(oneWord fieldTo twoWord.term)
		term(
			oneWord fieldTo twoWord.term,
			twoWord.field).onlyFieldOrNull.assertEqualTo(null)
	}

	@Test
	fun select_match() {
		term(
			nameWord fieldTo stringWord.term,
			ageWord fieldTo numberWord.term,
			nameWord.field)
			.select
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun select_mismatch() {
		term(
			nameWord fieldTo stringWord.term,
			ageWord fieldTo numberWord.term,
			oneWord.field)
			.select
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
			.select
			.assertEqualTo(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term))
	}

	@Test
	fun tokenStream() {
		testTerm
			.tokenStream
			.assertContains(
				oneWord.beginToken,
				end.token,
				negateWord.beginToken,
				end.token,
				plusWord.beginToken,
				twoWord.beginToken,
				end.token,
				end.token,
				plusWord.beginToken,
				ageWord.beginToken,
				end.token,
				personWord.beginToken,
				end.token,
				end.token)
	}
}