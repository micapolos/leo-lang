package leo.lab.v1

import leo.*
import leo.base.assertEqualTo
import leo.base.back
import org.junit.Test

class PatternTest {
	@Test
	fun matches_simplePattern() {
		val patternScript =
			script<Pattern>(
				personWord to script(
					firstWord to script(nameWord to stringWord.script),
					lastWord to script(nameWord to stringWord.script),
					ageWord to script(numberWord)))

		script(personWord to script(
			firstWord to script(nameWord to stringWord.script),
			lastWord to script(nameWord to stringWord.script),
			ageWord to script(numberWord)))
			.matches(patternScript)
			.assertEqualTo(true)

		script(personWord to script(
			firstWord to script(nameWord to oneWord.script),
			lastWord to script(nameWord to stringWord.script),
			ageWord to script(numberWord)))
			.matches(patternScript)
			.assertEqualTo(false)
	}

	@Test
	fun matches_oneOfPattern() {
		val patternScript =
			script(
				metaScript(pattern(oneOf(falseWord.script, trueWord.script))),
				andWord to metaScript(pattern(oneOf(falseWord.script, trueWord.script))))

		script(
			falseWord.script,
			andWord to falseWord.script)
			.matches(patternScript)
			.assertEqualTo(true)

		script(
			falseWord.script,
			andWord to trueWord.script)
			.matches(patternScript)
			.assertEqualTo(true)

		script(
			trueWord.script,
			andWord to falseWord.script)
			.matches(patternScript)
			.assertEqualTo(true)

		script(
			trueWord.script,
			andWord to trueWord.script)
			.matches(patternScript)
			.assertEqualTo(true)

		script(
			falseWord.script,
			orWord to falseWord.script)
			.matches(patternScript)
			.assertEqualTo(false)
	}

	val onesPatternScript: Script<Pattern> =
		metaScript(pattern(oneOf(
			script(oneWord to null),
			script(
				metaScript(pattern(recursion(back))),
				oneWord to null))))

	@Test
	fun one_matchesOnes() {
		oneWord.script
			.matches(onesPatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun one_one_matchesOnes() {
		script<Nothing>(
			oneWord to null,
			oneWord to null)
			.matches(onesPatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun one_one_one_matchesOnes() {
		script<Nothing>(
			oneWord to null,
			oneWord to null,
			oneWord to null)
			.matches(onesPatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun two_one_one_notMatchesOnes() {
		script<Nothing>(
			twoWord to null,
			oneWord to null,
			oneWord to null)
			.matches(onesPatternScript)
			.assertEqualTo(false)
	}

	val naturalPatternScript =
		metaScript(pattern(oneOf(
			zeroWord.script,
			script(
				metaScript(pattern(recursion(back))),
				plusWord to oneWord.script()))))

	@Test
	fun zero_matchesNatural() {
		zeroWord.script
			.matches(naturalPatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun zeroPlusOne_matchesNatural() {
		script(
			zeroWord to null,
			plusWord to oneWord.script)
			.matches(naturalPatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun zeroPlusOnePlusOne_matchesNatural() {
		script(
			zeroWord to null,
			plusWord to oneWord.script,
			plusWord to oneWord.script)
			.matches(naturalPatternScript)
			.assertEqualTo(true)
	}

	val treePatternScript =
		script(
			treeWord to metaScript(pattern(oneOf(
				script(
					leafWord to metaScript(pattern(oneOf(
						falseWord.script,
						trueWord.script)))),
				script(
					leftWord to metaScript(pattern(recursion(back, back, back))),
					rightWord to metaScript(pattern(recursion(back, back, back))))))))

	@Test
	fun treeLeafFalse_matchesTree() {
		script(treeWord to script(leafWord to falseWord.script))
			.matches(treePatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun treeLeafTrue_matchesTree() {
		script(treeWord to script(leafWord to trueWord.script))
			.matches(treePatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun treeLeafMaybe_notMatchesTree() {
		script(treeWord to script(leafWord to maybeWord.script))
			.matches(treePatternScript)
			.assertEqualTo(false)
	}

	@Test
	fun oneLevelTree_matchesTree() {
		script(
			treeWord to script(
				leftWord to script(
					treeWord to script(
						leafWord to falseWord.script)),
				rightWord to script(
					treeWord to script(
						leafWord to trueWord.script))))
			.matches(treePatternScript)
			.assertEqualTo(true)
	}

	@Test
	fun twoLevelTree_matchesTree() {
		script(
			treeWord to script(
				leftWord to script(
					treeWord to script(
						leftWord to script(
							treeWord to script(
								leafWord to falseWord.script)),
						rightWord to script(
							treeWord to script(
								leafWord to trueWord.script)))),
				rightWord to script(
					treeWord to script(
						leafWord to trueWord.script))))
			.matches(treePatternScript)
			.assertEqualTo(true)
	}
}