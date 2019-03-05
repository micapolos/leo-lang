package dsl

import dsl.impl.anyListScript
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class DslTest {
	@Test
	fun term() {
		one().assertEqualTo(listOf("one"))
	}

	@Test
	fun script() {
		program(
			programming(language(leo())),
			zero(), plus(one()),
			define(bit(), gives(either(bit(zero())), either(bit(one())))),
			define(boolean(), gives(either(true), either(false))),
			define(false, and(false), gives(false)),
			define(false, and(true), gives(false)),
			define(false, and(true), gives(false)),
			define(true, and(true), gives(true)))
			.anyListScript.string.assertEqualTo("")
	}
}