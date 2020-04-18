package leo15.type.core

import leo15.core.intTyp
import leo15.core.java
import leo15.core.linkTo
import leo15.core.list
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