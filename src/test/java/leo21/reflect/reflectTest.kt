package leo21.reflect

import leo.base.assertNotNull
import leo21.token.processor.emptyBodyProcessor
import kotlin.test.Test

class ReflectTest {
	@Test
	fun stack() {
		emptyBodyProcessor.refScriptLine.assertNotNull
	}
}