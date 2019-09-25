package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeLineTest {
	@Test
	fun recurseExpand() {
		("zero" lineTo type())
			.expand()
			.assertEqualTo("zero" lineTo type())

		("zero" lineTo type(onceRecurse))
			.expand()
			.assertEqualTo("zero" lineTo type("zero" lineTo type(onceRecurse)))
	}

	@Test
	fun rhs() {
		("bit" lineTo type("zero"))
			.rhs
			.assertEqualTo(type("zero"))

		("bit" lineTo type(onceRecurse))
			.rhs
			.assertEqualTo(type("bit" lineTo type(onceRecurse)))

		("bit" lineTo type("zero" lineTo type(onceRecurse)))
			.rhs
			.assertEqualTo(type("zero" lineTo type(onceRecurse)))

		("bit" lineTo type("zero" lineTo type(onceRecurse.increase)))
			.rhs
			.assertEqualTo(
				type(
					"zero" lineTo type(
						"bit" lineTo type(
							"zero" lineTo type(onceRecurse.increase)))))
	}
}
