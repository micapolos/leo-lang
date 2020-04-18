package leo15.type.core

import leo15.core.*
import kotlin.test.Test

class ListTest {
	@Test
	fun linkOrNull() {
		intTyp.list.optionalLink.assertGives(intTyp.linkTyp.absent)
		list(10.java).optionalLink.assertGives(intTyp.list.linkTo(10.java).present)
		list(10.java, 20.java).optionalLink.assertGives(list(10.java).linkTo(20.java).present)
	}

	@Test
	fun seq() {
		intTyp.list().assertContains()
		intTyp.list(10.java).assertContains(10.java)
		intTyp.list(10.java, 20.java).assertContains(10.java, 20.java)
		list(10.java, 20.java).assertContains(10.java, 20.java)
	}
}