package leo16.library

import leo.base.assertEqualTo
import leo13.stack
import leo15.importName
import leo16.*
import kotlin.test.Test

class IndexTest {
	@Test
	fun allTest() {
		valueFunMap.keys
			.map { importName(it.value) }
			.toTypedArray()
			.let { stack(*it) }
			.value
			.let { emptyScope.emptyEvaluator.plus(it).evaluated.value }
			.assertEqualTo(value())
	}
}