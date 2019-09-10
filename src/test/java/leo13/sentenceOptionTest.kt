package leo13

import leo.base.assertContains
import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import org.junit.Test

class SentenceOptionTest {
	@Test
	fun sentenceScriptFromLegacy() {
		sentenceOption(script())
			.assertEqualTo(sentenceOption())

		sentenceOption(
			script(
				"x" lineTo script("zero"),
				"y" lineTo script("one"),
				"point" lineTo script()))
			.assertEqualTo(
				sentenceOption(
					sentence(
						pointWord lineTo sentence(
							xWord lineTo sentence(zeroWord),
							yWord lineTo sentence(oneWord)))))
	}

	@Test
	fun legacyScript() {
		sentenceOption()
			.legacyScript
			.assertEqualTo(script())

		sentenceOption(
			sentence(
				pointWord lineTo sentence(
					xWord lineTo sentence(zeroWord),
					yWord lineTo sentence(oneWord))))
			.legacyScript
			.assertEqualTo(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
	}

	@Test
	fun lineSeq() {
		sentenceOption()
			.lineSeq
			.assertContains()

		sentenceOption(
			sentence(
				xWord lineTo sentence(zeroWord),
				yWord lineTo sentence(oneWord)))
			.lineSeq
			.assertContains(
				xWord lineTo option(sentence(zeroWord)),
				yWord lineTo option(sentence(oneWord)))

		sentenceOption(
			sentence(
				zeroWord,
				plusWord lineTo sentence(oneWord)))
			.lineSeq
			.assertContains(
				zeroWord lineTo sentenceOption(),
				plusWord lineTo option(sentence(oneWord)))

	}
}