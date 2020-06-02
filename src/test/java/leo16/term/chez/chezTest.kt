package leo16.term.chez

import leo.base.assertEqualTo
import leo16.term.value
import kotlin.test.Test

class ChezTest {
	@Test
	fun string() {
		"Hello, world!".chez.value()
			.string
			.assertEqualTo("\"Hello, world!\"")

		"Hello, world!".chez.value()
			.stringLength
			.string
			.assertEqualTo("(string-length \"Hello, world!\")")

		"Hello, ".chez.value()
			.stringPlus("world!".chez.value())
			.string
			.assertEqualTo("(((lambda (v0) (lambda (v1) (string-append v0 v1))) \"Hello, \") \"world!\")")
	}

	@Test
	fun int() {
		10.chez.value()
			.string
			.assertEqualTo("10")

		10.chez.value()
			.intPlus(20.chez.value())
			.string
			.assertEqualTo("(((lambda (v0) (lambda (v1) (fx+ v0 v1))) 10) 20)")

		10.chez.value()
			.intLessThan(20.chez.value())
			.string
			.assertEqualTo("(((lambda (v0) (lambda (v1) (fx< v0 v1))) 10) 20)")
	}
}