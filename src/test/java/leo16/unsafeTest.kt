package leo16

import leo.base.assertEqualTo
import leo15.lastName
import leo15.previousName
import kotlin.test.Test
import kotlin.test.assertFails

class UnsafeTest {
	@Test
	fun thing() {
		script("point"()).thing.assertEqualTo(script())

		script(
			"point"(
				"x"("zero"()),
				"y"("one"())))
			.thing
			.assertEqualTo(
				script(
					"x"("zero"()),
					"y"("one"())))

		assertFails { script().thing }
		assertFails { script("x"("zero"()), "y"("one"())).thing }
	}

	@Test
	fun get() {
		script(
			"circle"(
				"radius"("ten"()),
				"center"("point"(
					"x"("zero"()),
					"y"("one"())))))
			.run {
				get("radius")
					.assertEqualTo(script("radius"("ten"())))
				get("center")
					.assertEqualTo(
						script(
							"center"("point"(
								"x"("zero"()),
								"y"("one"())))))
				assertFails { get("color") }
			}
	}

	@Test
	fun make() {
		script(
			"x"("zero"()),
			"y"("one"()))
			.make("point")
			.assertEqualTo(
				script(
					"point"(
						"x"("zero"()),
						"y"("one"()))))
	}

	@Test
	fun list() {
		script("items"("zero"(), "one"(), "two"()))
			.run {
				listIsEmpty.assertEqualTo(false)
				last.assertEqualTo(script(lastName("two"())))
				previous.assertEqualTo(script(previousName("items"("zero"(), "one"()))))
				append("three"()).assertEqualTo(script("items"("zero"(), "one"(), "two"(), "three"())))
			}

		script("items"())
			.run {
				listIsEmpty.assertEqualTo(true)
				assertFails { last }
				assertFails { previous }
				append("zero"()).assertEqualTo(script("items"("zero"())))
			}
	}

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