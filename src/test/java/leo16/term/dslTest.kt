package leo16.term

import leo.base.assertNotNull
import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		lambda(at<Unit>(0)).invoke("foo".value()).assertNotNull
	}
}