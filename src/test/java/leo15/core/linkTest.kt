package leo15.core

import kotlin.test.Test

class LinkTest {
	@Test
	fun linkTo() {
		intTyp.list
			.linkTo(10.java)
			.run {
				tail.assertGives(intTyp.list)
				head.assertGives(10.java)
			}

		list(10.java)
			.linkTo(20.java)
			.run {
				tail.assertGives(list(10.java))
				head.assertGives(20.java)
			}
	}
}