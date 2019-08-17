package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeFunctionsTest {
	@Test
	fun get() {
		typeFunctions(
			function(
				type().linkTo("bit" lineTo type()),
				type("bit" lineTo type(
					choice("zero" lineTo type()),
					choice("one" lineTo type())))))
			.type(type().linkTo("bit" lineTo type("zero" lineTo type())))
			.assertEqualTo(
				type("bit" lineTo type(
					choice("zero" lineTo type()),
					choice("one" lineTo type()))))
	}
}