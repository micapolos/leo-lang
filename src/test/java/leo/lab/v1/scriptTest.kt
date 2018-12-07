package leo.lab.v1

import leo.*
import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.string
import org.junit.Test

class ScriptTest {
	@Test
	fun string() {
		script(
			oneWord.script,
			plusWord to twoWord.script,
			plusWord to script(
				twoWord.script,
				timesWord to twoWord.script))
			.string
			.assertEqualTo("one, plus two, plus(two, times two)")
	}

	@Test
	fun isSimple() {
		oneWord.script.isSimple.assertEqualTo(true)
		script(oneWord to twoWord.script).isSimple.assertEqualTo(true)
		1.meta.script.isSimple.assertEqualTo(true)
	}

	@Test
	fun tokenStream() {
		script(
			negateWord to oneWord.script,
			plusWord to script(
				ageWord to numberWord.script,
				nameWord to stringWord.script))
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

	@Test
	fun fieldStream() {
		1.meta.script
			.fieldStreamOrNull
			.assertContains()

		oneWord.script
			.fieldStreamOrNull
			.assertContains()

		oneWord.script.invoke(twoWord)
			.fieldStreamOrNull
			.assertContains()

		oneWord.script.invoke(twoWord to theWord.script)
			.fieldStreamOrNull
			.assertContains()

		script(oneWord to twoWord.script, threeWord to null)
			.fieldStreamOrNull
			.assertContains()

		script(oneWord to twoWord.script)
			.fieldStreamOrNull
			.assertContains(
				oneWord fieldTo twoWord.script)

		script(oneWord to twoWord.script, threeWord to fourWord.script)
			.fieldStreamOrNull
			.assertContains(
				oneWord fieldTo twoWord.script,
				threeWord fieldTo fourWord.script)

		script(
			oneWord to twoWord.script,
			twoWord to null,
			threeWord to fourWord.script)
			.fieldStreamOrNull
			.assertContains()
	}

	@Test
	fun matchAnyWord() {
		1.meta.script.matchWord { this }.assertEqualTo(null)
		oneWord.script.matchWord { this }.assertEqualTo(oneWord)
		script(oneWord to twoWord.script).matchWord { this }.assertEqualTo(null)
		script<Nothing>(oneWord to null, twoWord to null).matchWord { this }.assertEqualTo(null)
	}

	@Test
	fun matchWord() {
		1.meta.script.matchWord(oneWord) { Unit }.assertEqualTo(null)
		oneWord.script.matchWord(oneWord) { Unit }.assertEqualTo(Unit)
		twoWord.script.matchWord(oneWord) { Unit }.assertEqualTo(null)
		script(oneWord to twoWord.script).matchWord(oneWord) { Unit }.assertEqualTo(null)
		script<Nothing>(oneWord to null, twoWord to null).matchWord(oneWord) { Unit }.assertEqualTo(null)
	}

	@Test
	fun matchFieldKey() {
		1.meta.script.matchFieldKey(oneWord) { this }.assertEqualTo(null)
		oneWord.script.matchFieldKey(oneWord) { this }.assertEqualTo(null)
		script(oneWord to twoWord.script).matchFieldKey(oneWord) { this }.assertEqualTo(twoWord.script)
		script(oneWord to twoWord.script).matchFieldKey(twoWord) { this }.assertEqualTo(null)
		script(oneWord to twoWord.script, twoWord to theWord.script).matchFieldKey(oneWord) { this }.assertEqualTo(null)
	}

	@Test
	fun matchTwoFieldKeys() {
		1.meta.script.matchFieldKeys(oneWord, twoWord) { a, b -> a to b }.assertEqualTo(null)
		oneWord.script.matchFieldKeys(oneWord, twoWord) { a, b -> a to b }.assertEqualTo(null)
		script(oneWord to twoWord.script).matchFieldKeys(oneWord, twoWord) { a, b -> a to b }.assertEqualTo(null)
		script(oneWord to twoWord.script, twoWord to threeWord.script)
			.matchFieldKeys(oneWord, twoWord) { a, b -> a to b }.assertEqualTo(twoWord.script to threeWord.script)
		script(oneWord to twoWord.script, threeWord to twoWord.script)
			.matchFieldKeys(oneWord, twoWord) { a, b -> a to b }.assertEqualTo(null)
		script(zeroWord to oneWord.script, oneWord to twoWord.script, twoWord to theWord.script)
			.matchFieldKeys(oneWord, twoWord) { a, b -> a to b }.assertEqualTo(null)
	}

	@Test
	fun isList() {
		script(theWord to oneWord.script)
			.isList
			.assertEqualTo(true)

		script(
			theWord to oneWord.script,
			theWord to twoWord.script)
			.isList
			.assertEqualTo(true)

		script(
			theWord to oneWord.script,
			itWord to twoWord.script,
			theWord to threeWord.script)
			.isList
			.assertEqualTo(false)

		script(
			theWord to oneWord.script,
			theWord to null,
			theWord to threeWord.script)
			.isList
			.assertEqualTo(false)
	}

	@Test
	fun fieldValueStreamOrNull() {
		script(
			oneWord to twoWord.script,
			threeWord to fourWord.script,
			oneWord to fiveWord.script)
			.fieldValueStreamOrNull(oneWord)
			.assertContains(twoWord.script, fiveWord.script)
	}

	@Test
	fun select() {
		script<Int>(oneWord to null)
			.select(oneWord)
			.assertEqualTo(null)

		script(
			oneWord to twoWord.script,
			threeWord to fourWord.script)
			.select(oneWord)
			.assertEqualTo(twoWord.script)

		script(
			oneWord to twoWord.script,
			threeWord to fourWord.script)
			.select(threeWord)
			.assertEqualTo(fourWord.script)

		script(
			oneWord to twoWord.script,
			threeWord to fourWord.script,
			oneWord to fiveWord.script)
			.select(oneWord)
			.assertEqualTo(
				script(
					theWord to twoWord.script,
					theWord to fiveWord.script))

		script(
			oneWord to twoWord.script,
			threeWord to null,
			oneWord to fiveWord.script)
			.select(oneWord)
			.assertEqualTo(null)
	}
}