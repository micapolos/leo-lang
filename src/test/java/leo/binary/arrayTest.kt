package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class ArrayTest {
	@Test
	fun default() {
		"default".defaultArray32.at(13).assertEqualTo("default")
		"default".defaultArray32.put(13, "some").at(12).assertEqualTo("default")
		"default".defaultArray32.put(13, "some").at(13).assertEqualTo("some")
		"default".defaultArray32.put(13, "some").at(14).assertEqualTo("default")
	}
}