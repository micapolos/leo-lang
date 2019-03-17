package leo32

import leo.base.assertEqualTo
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class BranchTest {
	@Test
	fun print() {
		newVm.run {
			this
				.printString { print ->
					branch(print, branch(126, 127), Print::ptr)
				}
				.assertEqualTo("q23")
		}
	}

	@Test
	fun at() {
		newVm.run {
			val branch = branch(1, 2)
			branchAt(branch, zero.bit).assertEqualTo(1)
			branchAt(branch, one.bit).assertEqualTo(2)

			val branch0 = branchWith(branch, zero.bit, 3)
			branchAt(branch, zero.bit).assertEqualTo(1)
			branchAt(branch, one.bit).assertEqualTo(2)
			branchAt(branch0, zero.bit).assertEqualTo(3)
			branchAt(branch0, one.bit).assertEqualTo(2)

			val branch1 = branchWith(branch0, one.bit, 4)
			branchAt(branch, zero.bit).assertEqualTo(1)
			branchAt(branch, one.bit).assertEqualTo(2)
			branchAt(branch0, zero.bit).assertEqualTo(3)
			branchAt(branch0, one.bit).assertEqualTo(2)
			branchAt(branch1, zero.bit).assertEqualTo(3)
			branchAt(branch1, one.bit).assertEqualTo(4)
		}
	}
}