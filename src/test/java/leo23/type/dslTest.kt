package leo23.type

import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		booleanType
		textType
		numberType
		params(numberType, textType) arrowTo booleanType
		"point" struct fields(
			"x" struct fields(numberType),
			"y" struct fields(numberType))
		"boolean" choice cases(
			"true".type,
			"false".type)
	}
}