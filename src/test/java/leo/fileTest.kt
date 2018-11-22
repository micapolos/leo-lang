package leo

import leo.base.assertEqualTo
import java.nio.file.Paths
import kotlin.test.Test

class FileTest {
	@Test
	fun path() {
		file(oneWord, twoWord)
			.path
			.assertEqualTo(Paths.get("one", "two.leo"))
	}
}