package leo14.untyped

import leo.base.assertEqualTo
import leo13.base.linesString
import leo14.invoke
import leo14.script
import kotlin.test.Test

class DottedTest {
	@Test
	fun dottedString() {
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
}