package leo16.compiler

import leo.base.assertEqualTo
import leo16.Value
import leo16.invoke
import leo16.names.*
import leo16.value
import kotlin.test.Test

val Value.assertTypeSerializationWorks
	get() =
		typeOrNull!!.value.assertEqualTo(this)

class ValueTypeTest {
	@Test
	fun empty() {
		value().assertTypeSerializationWorks
	}

	@Test
	fun choice() {
		value(_choice(_case(_zero()), _case(_one())))
			.assertTypeSerializationWorks
	}

	@Test
	fun struct() {
		value(_point(_x(_zero())), _y(_one()))
			.assertTypeSerializationWorks
	}
}