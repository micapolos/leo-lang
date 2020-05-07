package leo14.untyped

import leo.base.assertEqualTo
import leo.base.runWith
import leo13.base.linesString
import leo14.*
import kotlin.test.Test

class DottedTest {
	@Test
	fun string() {
		dottedColorsParameter.runWith(false) {
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
				"mixed"(10, "foo"(), "bar"(), 20, "bar"()),
				"it"(123, "native"(), "string"()),
				"sequence"(
					1,
					"plus"(2),
					"plus"(3)),
				"a"(), "b"(),
				"plus"("c"()),
				"plus"("d"()))
				.leoString
				.assertEqualTo(
					if (useDots)
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
							"  20.bar",
							"it 123.native.string",
							"sequence",
							"  1",
							"  plus 2",
							"  plus 3",
							"a.b",
							"plus c",
							"plus d")
					else "circle\n" +
						"  radius 10\n" +
						"  center point\n" +
						"    x 20\n" +
						"    y 20\n" +
						"center\n" +
						"point\n" +
						"x\n" +
						"number\n" +
						"plus\n" +
						"  30\n" +
						"  times 40\n" +
						"numbers\n" +
						"  10\n" +
						"  20\n" +
						"  30\n" +
						"names\n" +
						"  foo\n" +
						"  bar\n" +
						"  zoo\n" +
						"mixed\n" +
						"  10\n" +
						"  foo\n" +
						"  bar\n" +
						"  20\n" +
						"  bar\n" +
						"it\n" +
						"  123\n" +
						"  native\n" +
						"  string\n" +
						"sequence\n" +
						"  1\n" +
						"  plus 2\n" +
						"  plus 3\n" +
						"a\n" +
						"b\n" +
						"plus c\n" +
						"plus d")
		}
	}

	@Test
	fun fragment() {
		emptyFragment
			.begin("circle")
			.leoString
			.assertEqualTo(
				linesString(
					"circle",
					"  "))
	}

	@Test
	fun fragmentBegin() {
		emptyFragment
			.begin("circle")
			.begin("radius")
			.leoString
			.assertEqualTo(
				linesString(
					"circle",
					"  radius",
					"    "))
	}

	@Test
	fun fragmentBeginEnd() {
		emptyFragment
			.begin("circle")
			.begin("radius")
			.end
			.leoString
			.assertEqualTo(
				linesString(
					"circle",
					"  radius",
					"  "))
	}

	@Test
	fun fragmentDotted() {
		dottedColorsParameter.runWith(false) {
			emptyFragment
				.plus(literal(10))
				.begin("inc")
				.end
				.leoString
				.assertEqualTo(
					if (useDots) linesString("10.inc", "")
					else linesString("10", "inc", ""))
		}
	}
}