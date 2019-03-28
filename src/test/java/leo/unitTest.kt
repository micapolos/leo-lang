package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class VmTest {
	@Test
	fun reflect() {
		Unit.reflect.assertEqualTo(unitWord fieldTo unitWord.term)
	}

	@Test
	fun reflectAndParse() {
		Unit.assertReflectAndParseWorks({ reflect }, { parseUnit })
	}
}