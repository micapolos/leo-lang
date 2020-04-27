package leo16

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class UnsafeTest {
	@Test
	fun match() {
		script("shape"("circle"("radius"("zero"()))))
			.match(
				"circle".gives { get("radius") },
				"square".gives { get("side") })
			.assertEqualTo(script("radius"("zero"())))

		script("shape"("square"("side"("zero"()))))
			.match(
				"circle".gives { get("radius") },
				"square".gives { get("side") })
			.assertEqualTo(script("side"("zero"())))

		assertFails {
			script("shape"("triangle"("side"("zero"()))))
				.match(
					"circle".gives { get("radius") },
					"square".gives { get("side") })
		}
	}
}