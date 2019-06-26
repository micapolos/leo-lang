package leo7

import leo.base.assertEqualTo
import leo.base.fold
import leo.base.orNull
import kotlin.test.Test

class ScriptParserTest {
	@Test
	fun read() {
		newScriptParser
			.orNull
			.fold("bit(zero())plus(bit(one()))") { this?.read(it) }!!
			.parsedScriptOrNull
			.assertEqualTo(
				script(
					bitWord lineTo script(
						zeroWord lineTo script()),
					plusWord lineTo script(
						bitWord lineTo script(
							oneWord lineTo script()))))
	}
}