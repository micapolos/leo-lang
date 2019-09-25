package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class TypeItemTest {
	@Test
	fun recurseExpand() {
		assertFails { item(onceRecurse).expand() }

		item(onceRecurse)
			.expand(root(onceRecurse, "foo" lineTo type()))
			.assertEqualTo(item("foo" lineTo type()))

		item(onceRecurse)
			.expand(root(onceRecurse.increase, "foo" lineTo type()))
			.assertEqualTo(item(onceRecurse))

		item("foo" lineTo type())
			.expand()
			.assertEqualTo(item("foo" lineTo type()))

		item("foo" lineTo type(onceRecurse))
			.expand()
			.assertEqualTo(item("foo" lineTo type("foo" lineTo type(onceRecurse))))
	}
}