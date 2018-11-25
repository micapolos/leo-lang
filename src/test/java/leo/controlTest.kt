package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class ControlTest {
	@Test
	fun beginReflect() {
		begin.control.reflect.assertEqualTo(controlWord fieldTo beginWord.term)
	}

	@Test
	fun endReflect() {
		end.control.reflect.assertEqualTo(controlWord fieldTo endWord.term)
	}

	@Test
	fun beginReflectAndParse() {
		begin.control.assertReflectAndParseWorks(Control::reflect, Field<Nothing>::parseControl)
	}

	@Test
	fun endReflectAndParse() {
		end.control.assertReflectAndParseWorks(Control::reflect, Field<Nothing>::parseControl)
	}
}
