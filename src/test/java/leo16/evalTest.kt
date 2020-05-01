package leo16

import leo.base.assertEqualTo
import leo15.dsl.*
import kotlin.test.Test

fun Script.assertGives(f: F) {
	assertEqualTo(read_(f))
}

class EvalTest {
	@Test
	fun appending() {
		evaluate_ { nothing_ }.assertGives { nothing_ }
		evaluate_ { zero }.assertGives { zero }
		evaluate_ { zero.plus { one } }.assertGives { zero.plus { one } }
	}

	@Test
	fun normalization() {
		evaluate_ { zero.negate }.assertGives { negate { zero } }
	}

	@Test
	fun script() {
		evaluate_ { script { nothing_ } }.assertGives { nothing_ }
		evaluate_ { script { zero.negate } }.assertGives { zero.negate }
		evaluate_ { script { zero.is_ { one } } }.assertGives { zero.is_ { one } }
	}

	@Test
	fun evaluate() {
		evaluate_ { script { nothing_ }.evaluate }.assertGives { nothing_ }
		evaluate_ { script { zero.negate }.evaluate }.assertGives { negate { zero } }
		evaluate_ { script { zero.is_ { one } }.evaluate }.assertGives { nothing_ }
		evaluate_ { script { zero.is_ { one }.zero }.evaluate }.assertGives { one }
		evaluate_ { script { zero.is_ { one } }.evaluate.zero }.assertGives { zero }
	}

	@Test
	fun compile() {
		evaluate_ { script { nothing_ }.compile }.assertGives { nothing_ }
		evaluate_ { script { zero.negate }.compile }.assertGives { negate { zero } }
		evaluate_ { script { zero.is_ { one } }.compile }.assertGives { nothing_ }
		evaluate_ { script { zero.is_ { one }.zero }.compile }.assertGives { one }
		evaluate_ { script { zero.is_ { one } }.compile.zero }.assertGives { one }
	}

	@Test
	fun get() {
		evaluate_ { point { x { zero }; y { one } }.x }.assertGives { x { zero } }
		evaluate_ { point { x { zero }; y { one } }.y }.assertGives { y { one } }
	}

	@Test
	fun thing() {
		evaluate_ { thing }.assertGives { thing }
		evaluate_ { point { x { zero }; y { one } }.thing }.assertGives { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; thing }.assertGives { thing { x { zero }; y { one } } }
	}

	@Test
	fun is_() {
		evaluate_ { zero.is_ { one } }.assertGives { nothing_ }
		evaluate_ { zero.is_ { one }.zero }.assertGives { one }
		evaluate_ { any.is_ { one }.zero }.assertGives { zero }
		evaluate_ { any.is_ { one }.any }.assertGives { one }
	}

	@Test
	fun gives() {
		evaluate_ { zero.gives { one } }.assertGives { nothing_ }
		evaluate_ { zero.gives { one }.zero }.assertGives { one }
		evaluate_ { zero.gives { one }.one }.assertGives { one }
		evaluate_ { zero.gives { given }.zero }.assertGives { given { zero } }
		evaluate_ { zero.gives { given }.one }.assertGives { one }
	}
}