package leo21.type

import leo.base.assertEqualTo
import kotlin.test.Test

class RecursiveResolveTest {
	@Test
	fun shift() {
		recursive(
			type(
				"list" lineTo type(
					"empty" lineTo type(),
					"tail" lineTo type(recurse(0)))))
			.resolve
			.assertEqualTo(
				type(
					"list" lineTo type(
						recursive(
							type(
								"empty" lineTo type(),
								"tail" lineTo type(
									"list" lineTo type(recurse(0))))))))
	}
}