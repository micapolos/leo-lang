package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class ArrTest {
	@Test
	fun default() {
		"default".arr32.at(13).assertEqualTo("default")
		"default".arr32.put(13, "some").at(12).assertEqualTo("default")
		"default".arr32.put(13, "some").at(13).assertEqualTo("some")
		"default".arr32.put(13, "some").at(14).assertEqualTo("default")
	}
}