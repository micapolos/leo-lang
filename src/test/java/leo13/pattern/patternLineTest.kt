package leo13.pattern

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternLineTest {
	@Test
	fun recurseExpand() {
		("zero" lineTo pattern())
			.expand()
			.assertEqualTo("zero" lineTo pattern())

		("zero" lineTo pattern(onceRecurse))
			.expand()
			.assertEqualTo("zero" lineTo pattern("zero" lineTo pattern(onceRecurse)))
	}

	@Test
	fun rhs() {
		("bit" lineTo pattern("zero"))
			.rhs
			.assertEqualTo(pattern("zero"))

		("bit" lineTo pattern(onceRecurse))
			.rhs
			.assertEqualTo(pattern("bit" lineTo pattern(onceRecurse)))

		("bit" lineTo pattern("zero" lineTo pattern(onceRecurse)))
			.rhs
			.assertEqualTo(pattern("zero" lineTo pattern(onceRecurse)))

		("bit" lineTo pattern("zero" lineTo pattern(onceRecurse.increase)))
			.rhs
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"bit" lineTo pattern(
							"zero" lineTo pattern(onceRecurse.increase)))))
	}
}
