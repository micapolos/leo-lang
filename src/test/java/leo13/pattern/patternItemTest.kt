package leo13.pattern

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class PatternItemTest {
	@Test
	fun recurseExpand() {
		assertFails { item(onceRecurse).expand() }

		item(onceRecurse)
			.expand(root(onceRecurse, "foo" lineTo pattern()))
			.assertEqualTo(item("foo" lineTo pattern()))

		item(onceRecurse)
			.expand(root(onceRecurse.increase, "foo" lineTo pattern()))
			.assertEqualTo(item(onceRecurse))

		item("foo" lineTo pattern())
			.expand()
			.assertEqualTo(item("foo" lineTo pattern()))

		item("foo" lineTo pattern(onceRecurse))
			.expand()
			.assertEqualTo(item("foo" lineTo pattern("foo" lineTo pattern(onceRecurse))))
	}
}