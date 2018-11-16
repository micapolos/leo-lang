package leo.lab

import leo.*
import leo.base.assertEqualTo
import leo.base.nullOf
import leo.base.stack
import leo.base.string
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
		oneWord fieldTo metaTerm(1),
		ageWord fieldTo metaTerm(42),
		ageWord fieldTo metaTerm(43),
		twoWord fieldTo metaTerm(2)
	)

	@Test
	fun only() {
		termForGet.only(oneWord).assertEqualTo(metaTerm(1))
		termForGet.only(twoWord).assertEqualTo(metaTerm(2))
		termForGet.only(ageWord).assertEqualTo(null)
		termForGet.only(nameWord).assertEqualTo(null)
	}

	@Test
	fun all() {
		termForGet.all(oneWord).assertEqualTo(stack(metaTerm(1)))
		termForGet.all(twoWord).assertEqualTo(stack(metaTerm(2)))
		termForGet.all(ageWord).assertEqualTo(stack(metaTerm(42), metaTerm(43)))
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
			.assertEqualTo(null)
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
			.assertEqualTo(null)
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
			.assertEqualTo(metaTerm(1))
	}

	@Test
	fun selectMultiple() {
		termForGet
			.select(ageWord)
			.assertEqualTo(
				term(
					previousWord fieldTo term(
						lastWord fieldTo metaTerm(42)),
					lastWord fieldTo metaTerm(43)))
	}

	@Test
	fun selectMissing() {
		termForGet
			.select(personWord)
			.assertEqualTo(null)
	}
}