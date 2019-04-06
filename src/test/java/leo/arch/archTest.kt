package leo.arch

import leo.base.assertEqualTo
import leo.base.runAll
import leo32.base.i32
import leo32.base.i64
import kotlin.test.Test

class ArchTest {
	@Test
	fun zero() {
		arch32.zero.assertEqualTo(0.i32.word)
		arch64.zero.assertEqualTo(0.i64.word)
	}

	@Test
	fun max() {
		arch32.max.assertEqualTo((-1).i32.word)
		arch64.max.assertEqualTo((-1L).i64.word)
	}

	@Test
	fun bits() {
		arch32.bits.assertEqualTo(32.i32.word)
		arch64.bits.assertEqualTo(64.i64.word)
	}
}

fun archTest(fn: Arch.() -> Unit) =
	archSeq.runAll { fn() }