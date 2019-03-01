package dsl

import dsl.impl.Term
import dsl.impl.script
import leo.base.assertEqualTo
import kotlin.test.Test

class DslTest {
	@Test
	fun term() {
		one.assertEqualTo(Term("one", listOf()))
	}

	@Test
	fun script() {
		program(
			programming(language(leo())),
			zero,
			plus(one),
			define(
				bit,
				gives(
					either(bit(zero)),
					either(bit(one)))),
			define(
				boolean,
				gives(
					either(true),
					either(false))),
			define(
				false,
				and(false),
				gives(false)),
			define(
				false,
				and(true),
				gives(false)),
			define(
				false,
				and(true),
				gives(false)),
			define(
				true,
				and(true),
				gives(true))).script
	}
}