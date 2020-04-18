package leo15.typed

import leo.base.assertEqualTo
import leo15.plusName
import kotlin.test.Test

class TypedTest {
	@Test
	fun append() {
		runLeo {
			"Hello, ".typed
				.apply(plusName, "world!".typed)
				.eval
				.assertEqualTo("Hello, world!".typed)

			10.typed
				.apply(plusName, 20.typed)
				.eval
				.assertEqualTo(30.typed)
		}
	}
}