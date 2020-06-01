package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class DefinitionParseTest {
	@Test
	fun doesDefinition() {
		emptyDictionary
			.doesDefinitionOrNull(value(_zero(), _does(_one())))
			.assertEqualTo(value(_zero()).functionTo(value(_one()).compiled).definition)
	}
}