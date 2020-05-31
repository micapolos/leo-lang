package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValueOpsTest {
	@Test
	fun lazyForce() {
		value(_color(_red()))
			.compiled.lazy.field.value
			.force
			.assertEqualTo(value(_color(_red())))
	}

	@Test
	fun lazyThing() {
		value(_color(_red()))
			.compiled.lazy.field.value
			.thingOrNull
			.assertEqualTo(value(_red()))
	}

	@Test
	fun lazyGet() {
		value(_point(_x(_zero()), _y(_one())))
			.compiled.lazy.field.value
			.getOrNull(_x)
			.assertEqualTo(value(_x(_zero())))
	}
}