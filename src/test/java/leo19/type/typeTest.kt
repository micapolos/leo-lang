package leo19.type

import leo.base.assertEqualTo
import leo.base.indexed
import leo19.typed.indexedOrNull
import kotlin.test.Test

class TypeTest {
	@Test
	fun indexedOrNull() {
		type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo choice()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed type("x" fieldTo choice()))

		type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo choice()))
			.indexedOrNull("y")
			.assertEqualTo(1 indexed type("y" fieldTo choice()))

		type("point" fieldTo type("x" fieldTo type(), "y" fieldTo choice()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed type("x" fieldTo type()))

		type("point" fieldTo type("x" fieldTo type(), "y" fieldTo choice()))
			.indexedOrNull("y")
			.assertEqualTo(0 indexed type("y" fieldTo choice()))

		type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo type()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed type("x" fieldTo choice()))

		type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo type()))
			.indexedOrNull("y")
			.assertEqualTo(1 indexed type("y" fieldTo type()))

		type("point" fieldTo type("x" fieldTo type(), "y" fieldTo type()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed type("x" fieldTo type()))

		type("point" fieldTo type("x" fieldTo type(), "y" fieldTo type()))
			.indexedOrNull("y")
			.assertEqualTo(0 indexed type("y" fieldTo type()))
	}
}