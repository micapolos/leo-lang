package leo13

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import org.junit.Test

class SentenceScriptTest {
	@Test
	fun sentenceScriptFromLegacy() {
		sentenceScript(script())
			.assertEqualTo(sentenceScript())

		sentenceScript(
			script(
				"x" lineTo script("zero"),
				"y" lineTo script("one"),
				"point" lineTo script()))
			.assertEqualTo(
				sentenceScript(
					sentence(
						pointWord lineTo sentence(
							xWord lineTo sentence(zeroWord),
							yWord lineTo sentence(oneWord)))))
	}

	@Test
	fun legacyScript() {
		sentenceScript()
			.legacyScript
			.assertEqualTo(script())

		sentenceScript(
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
}