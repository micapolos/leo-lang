package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import org.junit.Test

class LangTest {
	@Test
	fun letterCaseTo() {
		function(letterCaseTo(template(okWord.script).body.match))
			.invoke(script(letterWord to aWord.script))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun wordCaseTo_oneLetter() {
		function(wordCaseTo(template(okWord.script).body.match))
			.invoke(
				script(
					wordWord to script(
						letterWord to aWord.script)))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun wordCaseTo_manyLetters() {
		function(wordCaseTo(template(okWord.script).body.match))
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
		function(bitCaseTo(template(okWord.script).body.match))
			.invoke(script(bitWord to zeroWord.script))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun bitOneInvoke() {
		function(bitCaseTo(template(okWord.script).body.match))
			.invoke(script(bitWord to oneWord.script))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun bitTwoInvoke() {
		function(bitCaseTo(template(okWord.script).body.match))
			.invoke(script(bitWord to twoWord.script))
			.assertEqualTo(script(bitWord to twoWord.script))
	}

	@Test
	fun byteInvoke() {
		function(byteCaseTo(template(okWord.script).body.match))
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