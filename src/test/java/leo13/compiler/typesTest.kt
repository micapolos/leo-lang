package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.caseTo
import leo13.type.lineTo
import leo13.type.type
import leo13.type.unsafeChoice
import kotlin.test.Test

class TypesTest {
	@Test
	fun lookup() {
		val bitTrace = type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "one" caseTo type())))

		types()
			.plus(bitTrace)
			.apply {
				resolve(type("bit" lineTo type("zero" lineTo type()))).assertEqualTo(bitTrace)
				resolve(type("bit" lineTo type("one" lineTo type()))).assertEqualTo(bitTrace)
				resolve(type("bit" lineTo type("two" lineTo type()))).assertEqualTo(
					type("bit" lineTo type("two" lineTo type())))

				resolve(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "one" caseTo type()))))
					.assertEqualTo(bitTrace)
				resolve(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "two" caseTo type()))))
					.assertEqualTo(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "two" caseTo type()))))
			}
	}
}