package leo16.reducers

import leo.base.assertEqualTo
import leo16.base.string
import leo16.base.text
import org.junit.Test

class TextTest {
	@Test
	fun indented() {
		"foo".text.charReducer.charReduce("bar").reduced.string.assertEqualTo("foobar")
	}
}