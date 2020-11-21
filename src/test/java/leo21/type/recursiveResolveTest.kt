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
}