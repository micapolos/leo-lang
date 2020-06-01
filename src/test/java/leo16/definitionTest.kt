package leo16

import leo.base.assertEqualTo
import leo13.assertContains
import leo16.names.*
import kotlin.test.Test

class DefinitionTest {
	@Test
	fun valueParameterDefinitionStack() {
		value(_x(_zero()), _y(_one()))
			.parameterDefinitionStack
			.assertContains(
				_x().onlyValue.is_(value(_x(_zero()))).definition,
				_y().onlyValue.is_(value(_y(_one()))).definition)
	}

	@Test
	fun thingParameterDefinition() {
		value(_x(_zero()), _y(_one()))
			.thingParameterDefinition
			.assertEqualTo(_thing().onlyValue.is_(value(_x(_zero()), _y(_one()))).definition)
	}

	@Test
	fun applyIs() {
		value(_zero())
			.is_(value(_one()))
			.definition
			.apply(value(_zero()).evaluated)
			.assertEqualTo(value(_one()).evaluated)

		value(_zero())
			.is_(value(_one()))
			.definition
			.apply(value(_one()).evaluated)
			.assertEqualTo(null)
	}

	@Test
	fun applyDoes() {
		value(_zero())
			.functionTo(value(_one()).compiled)
			.definition
			.apply(value(_zero()).evaluated)
			.assertEqualTo(value(_one()).evaluated)
	}

	@Test
	fun applyDoesText() {
		value(_check(_text(_any(_native()))))
			.functionTo(value(_ok()).compiled)
			.definition
			.apply(value(_check(_text("hello".nativeValue))).evaluated)
			.assertEqualTo(value(_ok()).evaluated)
	}
}