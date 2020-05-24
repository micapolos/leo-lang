package leo16.compiler

import leo.base.assertEqualTo
import leo16.invoke
import leo16.names.*
import leo16.value
import org.junit.Test

class TypeSizeTest {
	@Test
	fun static() {
		_point(_x(), _y())
			.value
			.typeOrNull!!
			.size
			.assertEqualTo(0)
	}

	@Test
	fun choiceEvenCases() {
		_choice(_case(_absent()), _case(_present()))
			.value
			.typeOrNull!!
			.size
			.assertEqualTo(4)
	}

	@Test
	fun choiceUnevenCases() {
		_choice(_case(_absent()), _case(_present(_choice(_case(_zero()), _case(_one())))))
			.value
			.typeOrNull!!
			.size
			.assertEqualTo(8)
	}
}