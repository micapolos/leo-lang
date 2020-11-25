package leo23.type

import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		nilType
		booleanType
		textType
		numberType
		textType.vector
		numberType pairTo textType
		textType.orNil
		params(numberType, textType) arrowTo booleanType
	}
}