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
}