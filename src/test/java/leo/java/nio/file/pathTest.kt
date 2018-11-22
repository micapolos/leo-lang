package leo.java.nio.file

import leo.base.assertEqualTo
import leo.oneWord
import leo.selector
import leo.twoWord
import java.nio.file.Paths
import kotlin.test.Test

class PathTest {
	@Test
	fun wordPath() {
		oneWord.path.assertEqualTo(Paths.get("one"))
	}

	@Test
	fun emptySelectorPath() {
		selector().path.assertEqualTo(Paths.get(""))
	}

	@Test
	fun nonEmptySelectorPath() {
		selector(oneWord, twoWord).path.assertEqualTo(Paths.get("one", "two"))
	}

	@Test
	fun emptySelectorLeoPath() {
		selector().leoPath.assertEqualTo(Paths.get("leo"))
	}

	@Test
	fun nonEmptySelectorLeoPath() {
		selector(oneWord, twoWord).leoPath.assertEqualTo(Paths.get("one", "two.leo"))
	}
}