package leo.term.dsl

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class DefsTest {
	@Test
	fun bit() {
		_bit.string.assertEqualTo("bit, gives(either zero, either one)")
	}

	@Test
	fun naturalNumber() {
		_naturalNumber.string.assertEqualTo("natural number, gives(either natural number zero, either(natural number, plus one))")
	}
}