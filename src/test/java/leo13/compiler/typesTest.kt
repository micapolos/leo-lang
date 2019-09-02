package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.caseTo
import leo13.type.lineTo
import leo13.type.type
import leo13.type.unsafeChoice
import kotlin.test.Test

class TypeFunctionsTest {
	@Test
	fun lookup() {
		val bitTypeFunction = function(
			"bit",
			type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "one" caseTo type()))))

		typeFunctions()
			.plus(bitTypeFunction)
			.apply {
				resolve(type("bit" lineTo type("zero" lineTo type()))).assertEqualTo(bitTypeFunction.type)
				resolve(type("bit" lineTo type("one" lineTo type()))).assertEqualTo(bitTypeFunction.type)
				resolve(type("bit" lineTo type("two" lineTo type()))).assertEqualTo(
					type("bit" lineTo type("two" lineTo type())))

				resolve(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "one" caseTo type()))))
					.assertEqualTo(bitTypeFunction.type)
				resolve(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "two" caseTo type()))))
					.assertEqualTo(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "two" caseTo type()))))
			}
	}
}