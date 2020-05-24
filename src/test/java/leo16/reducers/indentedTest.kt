package leo16.reducers

import leo.base.assertEqualTo
import kotlin.test.Test

class IndentedTest {
	@Test
	fun indented() {
		reduceString {
			indented(3).charReduce("foo\nbar\nzoo")
		}.assertEqualTo("foo\n      bar\n      zoo")
	}
}