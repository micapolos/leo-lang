package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.caseTo
import leo13.type.lineTo
import leo13.type.type
import leo13.type.unsafeChoice
import kotlin.test.Test

class TracesTest {
	@Test
	fun lookup() {
		val bitTrace = trace(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "one" caseTo type()))))

		traces()
			.plus(bitTrace)
			.apply {
				resolve(trace(type("bit" lineTo type("zero" lineTo type())))).assertEqualTo(bitTrace)
				resolve(trace(type("bit" lineTo type("one" lineTo type())))).assertEqualTo(bitTrace)
				resolve(trace(type("bit" lineTo type("two" lineTo type())))).assertEqualTo(
					trace(type("bit" lineTo type("two" lineTo type()))))

				resolve(trace(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "one" caseTo type())))))
					.assertEqualTo(bitTrace)
				resolve(trace(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "two" caseTo type())))))
					.assertEqualTo(trace(type("bit" lineTo type(unsafeChoice("zero" caseTo type(), "two" caseTo type())))))
			}
	}
}