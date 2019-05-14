package leo3

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class ScripTest {
	@Test
	fun string() {
		script(
			line(word("circle"), script(
				line(word("radius"), script(line(word("10")))),
				line(word("center"), script(
					line(word("x"), script(line(word("12")))),
					line(word("y"), script(line(word("13")))))))))
			.string
			.assertEqualTo("circle(radius(10())center(x(12())y(13())))")
	}
}