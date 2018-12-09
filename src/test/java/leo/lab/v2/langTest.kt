package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import org.junit.Test

class LangTest {
	@Test
	fun letterCaseTo() {
		pattern(letterCaseTo(match(template(okWord.script))))
			.invoke(script(letterWord to aWord.script))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun wordCaseTo_oneLetter() {
		pattern(wordCaseTo(match(template(okWord.script))))
			.invoke(
				script(
					wordWord to script(
						letterWord to aWord.script)))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun wordCaseTo_manyLetters() {
		pattern(wordCaseTo(match(template(okWord.script))))
			.invoke(
				script(
					wordWord to script(
						letterWord to aWord.script,
						letterWord to bWord.script,
						letterWord to cWord.script)))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun bitZeroInvoke() {
		pattern(bitCaseTo(match(template(okWord.script))))
			.invoke(script(bitWord to zeroWord.script))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun bitOneInvoke() {
		pattern(bitCaseTo(match(template(okWord.script))))
			.invoke(script(bitWord to oneWord.script))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun bitTwoInvoke() {
		pattern(bitCaseTo(match(template(okWord.script))))
			.invoke(script(bitWord to twoWord.script))
			.assertEqualTo(script(bitWord to twoWord.script))
	}

	@Test
	fun byteInvoke() {
		pattern(byteCaseTo(match(template(okWord.script))))
			.invoke(
				script(
					byteWord to script(
						bitWord to zeroWord.script,
						bitWord to zeroWord.script,
						bitWord to zeroWord.script,
						bitWord to zeroWord.script,
						bitWord to oneWord.script,
						bitWord to oneWord.script,
						bitWord to zeroWord.script,
						bitWord to oneWord.script)))
			.assertEqualTo(okWord.script)
	}
}