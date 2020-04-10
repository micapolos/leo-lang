package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.untyped.isName
import kotlin.test.Test

class BindingTest {
	@Test
	fun apply() {
//		binding(1.valueSelfTyped, 2.typed)
//			.apply(1.valueSelfTyped)
//			.typedAssertEqualTo(2.typed)
//
//		binding(intType, 2.typed)
//			.apply(1.valueSelfTyped)
//			.typedAssertEqualTo(2.typed)
	}

	@Test
	fun compiled_bindingOrNull() {
		type(
			"x" lineTo emptyType,
			isName lineTo numberType2)
			.compiled(123)
			.bindingOrNull!!
			.run {
				keyType.assertEqualTo(type("x" lineTo emptyType))
				valueCompiled.assertEqualTo(numberType2.compiled(123))
			}
	}
}