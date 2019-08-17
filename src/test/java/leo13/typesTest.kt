package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TypesTest {
	@Test
	fun lookup() {
		val bitType = type("bit" lineTo type(choice("zero" lineTo type(), "one" lineTo type())))

		types()
			.plus(bitType)
			.apply {
				containingType(type("bit" lineTo type("zero" lineTo type()))).assertEqualTo(bitType)
				containingType(type("bit" lineTo type("one" lineTo type()))).assertEqualTo(bitType)
				containingType(type("bit" lineTo type("two" lineTo type()))).assertEqualTo(
					type("bit" lineTo type("two" lineTo type())))

				containingType(type("bit" lineTo type(choice("zero" lineTo type(), "one" lineTo type()))))
					.assertEqualTo(bitType)
				containingType(type("bit" lineTo type(choice("zero" lineTo type(), "two" lineTo type()))))
					.assertEqualTo(type("bit" lineTo type(choice("zero" lineTo type(), "two" lineTo type()))))
			}
	}
}