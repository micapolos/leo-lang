package leo15.type.core

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.assertNull
import leo15.core.*
import kotlin.test.Test

class ListTest {
	@Test
	fun equality() {
		intTyp.list.assertEqualTo(intTyp.list)
		intTyp.list.plus(10.java).assertEqualTo(intTyp.list.plus(10.java))
	}

	@Test
	fun linkOrNull() {
		intTyp.list.linkOrNull.assertNull
		list(10.java).linkOrNull.assertEqualTo(intTyp.list.linkTo(10.java))
	}

	@Test
	fun seq() {
		intTyp.list().seq.assertContains()
		intTyp.list(10.java, 20.java).seq.assertContains(20.java, 10.java)
		list(10.java, 20.java).seq.assertContains(20.java, 10.java)
	}
}