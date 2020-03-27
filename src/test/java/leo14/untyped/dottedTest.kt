package leo14.untyped

import leo.base.assertEqualTo
import leo13.base.linesString
import leo14.*
import kotlin.test.Test

class DottedTest {
	@Test
	fun string() {
		script(
			"circle"(
				"radius"(10),
				"center"(
					"point"(
						"x"(20),
						"y"(20)))),
			"center"(), "point"(), "x"(), "number"(),
			"plus"(30, "times"(40)),
			"numbers"(10, 20, 30),
			"names"("foo"(), "bar"(), "zoo"()),
			"mixed"(10, "foo"(), "bar"(), 20, "bar"()))
			.dottedString
			.assertEqualTo(
				linesString(
					"circle",
					"  radius 10",
					"  center point",
					"    x 20",
					"    y 20",
					"center.point.x.number",
					"plus",
					"  30",
					"  times 40",
					"numbers",
					"  10",
					"  20",
					"  30",
					"names foo.bar.zoo",
					"mixed",
					"  10.foo.bar",
					"  20.bar"))
	}

	@Test
	fun fragment() {
		emptyFragment
			.begin("circle")
			.dottedString
			.assertEqualTo("circle ")
	}

	@Test
	fun fragmentBegin() {
		emptyFragment
			.begin("circle")
			.begin("radius")
			.dottedString
			.assertEqualTo("circle radius ")
	}

	@Test
	fun fragmentBeginEnd() {
		emptyFragment
			.begin("circle")
			.begin("radius")
			.end
			.dottedString
			.assertEqualTo(
				linesString(
					"circle",
					"  radius",
					"  "))
	}

	@Test
	fun fragmentDotted() {
		emptyFragment
			.plus(literal(10))
			.begin("inc")
			.end
			.dottedString
			.assertEqualTo(
				linesString(
					"10.inc",
					""))
	}
}