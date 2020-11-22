package leo21.type

import leo.base.assertEqualTo
import kotlin.test.Test

class RecursiveResolveTest {
	@Test
	fun shift() {
		recursive(
			"list" lineTo type(
				"empty" lineTo type(),
				"tail" lineTo type(line(recurse(0)))))
			.resolve
			.assertEqualTo(
				"list" lineTo type(
					line(recursive("empty" lineTo type())),
					line(recursive("tail" lineTo type(
						"list" lineTo type(
							"empty" lineTo type(),
							line(recurse(0))))))))
	}

	@Test
	fun shift_empty() {
		recursive("empty" lineTo type())
			.resolve
			.assertEqualTo("empty" lineTo type())
	}

	@Test
	fun shift_infinite() {
		recursive("empty" lineTo type(line(recurse(0))))
			.resolve
			.assertEqualTo("empty" lineTo type(line(recursive("empty" lineTo type(line(recurse(0)))))))
	}
}