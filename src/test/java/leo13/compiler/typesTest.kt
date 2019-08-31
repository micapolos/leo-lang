package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.*
import kotlin.test.Test

class TypesTest {
	@Test
	fun lookup() {
		val bitTrace = type(pattern("bit" lineTo pattern(unsafeChoice("zero" caseTo pattern(), "one" caseTo pattern()))))

		types()
			.plus(bitTrace)
			.apply {
				resolve(type(pattern("bit" lineTo pattern("zero" lineTo pattern())))).assertEqualTo(bitTrace)
				resolve(type(pattern("bit" lineTo pattern("one" lineTo pattern())))).assertEqualTo(bitTrace)
				resolve(type(pattern("bit" lineTo pattern("two" lineTo pattern())))).assertEqualTo(
					type(pattern("bit" lineTo pattern("two" lineTo pattern()))))

				resolve(type(pattern("bit" lineTo pattern(unsafeChoice("zero" caseTo pattern(), "one" caseTo pattern())))))
					.assertEqualTo(bitTrace)
				resolve(type(pattern("bit" lineTo pattern(unsafeChoice("zero" caseTo pattern(), "two" caseTo pattern())))))
					.assertEqualTo(type(pattern("bit" lineTo pattern(unsafeChoice("zero" caseTo pattern(), "two" caseTo pattern())))))
			}
	}
}