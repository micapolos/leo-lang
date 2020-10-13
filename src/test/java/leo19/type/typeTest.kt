package leo19.type

import leo.base.assertEqualTo
import leo.base.indexed
import leo19.typed.indexedOrNull
import kotlin.test.Test

class TypeTest {
	@Test
	fun indexedOrNull() {
		struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo choice()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed struct("x" fieldTo choice()))

		struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo choice()))
			.indexedOrNull("y")
			.assertEqualTo(1 indexed struct("y" fieldTo choice()))

		struct("point" fieldTo struct("x" fieldTo struct(), "y" fieldTo choice()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed struct("x" fieldTo struct()))

		struct("point" fieldTo struct("x" fieldTo struct(), "y" fieldTo choice()))
			.indexedOrNull("y")
			.assertEqualTo(0 indexed struct("y" fieldTo choice()))

		struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo struct()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed struct("x" fieldTo choice()))

		struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo struct()))
			.indexedOrNull("y")
			.assertEqualTo(1 indexed struct("y" fieldTo struct()))

		struct("point" fieldTo struct("x" fieldTo struct(), "y" fieldTo struct()))
			.indexedOrNull("x")
			.assertEqualTo(0 indexed struct("x" fieldTo struct()))

		struct("point" fieldTo struct("x" fieldTo struct(), "y" fieldTo struct()))
			.indexedOrNull("y")
			.assertEqualTo(0 indexed struct("y" fieldTo struct()))
	}
}