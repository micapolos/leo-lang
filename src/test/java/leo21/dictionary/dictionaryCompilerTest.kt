package leo21.dictionary

import leo.base.assertNotNull
import leo14.lineTo
import leo14.script
import leo21.compiled.emptyBindings
import kotlin.test.Test

class DictionaryCompilerTest {
	@Test
	fun plusDefineDoes() {
		emptyBindings
			.emptyDictionaryCompiler
			.plusOrNull(
				"define" lineTo script(
					"x" lineTo script("number"),
					"does" lineTo script("x")))
			.assertNotNull
	}

	@Test
	fun plusDefineGives() {
		emptyBindings
			.emptyDictionaryCompiler
			.plusOrNull(
				"define" lineTo script(
					"x" lineTo script("number"),
					"gives" lineTo script("x")))
			.assertNotNull
	}
}