package leo.base

import kotlin.test.Test

class FirstThenTest {
	@Test
	fun then() {
		1 then 2 then 3 assertEqualTo FirstThen(FirstThen(1, 2), 3)
		1 then all(2, 3, 4) assertEqualTo FirstThen(1, all(2, 3, 4))
	}
}