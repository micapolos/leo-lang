package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class TypesTest {
	@Test
	fun lookup() {
		val bitType = type("bit" lineTo type(unsafeChoice("zero" eitherTo type(), "one" eitherTo type())))

		leo13.type.types()
			.plus(bitType)
			.apply {
				containingType(type("bit" lineTo type("zero" lineTo type()))).assertEqualTo(bitType)
				containingType(type("bit" lineTo type("one" lineTo type()))).assertEqualTo(bitType)
				containingType(type("bit" lineTo type("two" lineTo type()))).assertEqualTo(
					type("bit" lineTo type("two" lineTo type())))

				containingType(type("bit" lineTo type(unsafeChoice("zero" eitherTo type(), "one" eitherTo type()))))
					.assertEqualTo(bitType)
				containingType(type("bit" lineTo type(unsafeChoice("zero" eitherTo type(), "two" eitherTo type()))))
					.assertEqualTo(type("bit" lineTo type(unsafeChoice("zero" eitherTo type(), "two" eitherTo type()))))
			}
	}
}