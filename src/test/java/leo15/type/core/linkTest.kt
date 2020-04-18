package leo15.type.core

import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import leo15.core.intTyp
import leo15.core.java
import leo15.core.linkTo
import leo15.core.list
import kotlin.test.Test

class LinkTest {
	@Test
	fun equality() {
		intTyp.list.linkTo(10.java).assertEqualTo(intTyp.list.linkTo(10.java))
		intTyp.list.linkTo(10.java).assertNotEqualTo(intTyp.list.linkTo(11.java))
	}

	@Test
	fun linkTo() {
		intTyp.list
			.linkTo(10.java)
			.run {
				tail.assertEqualTo(intTyp.list)
				head.assertEqualTo(10.java)
			}
	}
}