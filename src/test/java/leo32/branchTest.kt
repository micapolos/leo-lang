package leo32

import leo.base.assertEqualTo
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class BranchTest {
	@Test
	fun branch() {
		emptyBranch.assertEqualTo(null)
		branch(null, null).assertEqualTo(null)
		branch(0, null).assertEqualTo(Branch(0, null))
		branch(null, 1).assertEqualTo(Branch(null, 1))
		branch(0, 1).assertEqualTo(Branch(0, 1))

		emptyBranch.branchAt(zero.bit).assertEqualTo(null)
		emptyBranch.branchAt(one.bit).assertEqualTo(null)

		branch(10, null).branchPut(zero.bit, null).assertEqualTo(null)
		branch(10, null).branchPut(one.bit, null).assertEqualTo(branch(10, null))

		branch(null, 20).branchPut(zero.bit, null).assertEqualTo(branch(null, 20))
		branch(null, 20).branchPut(one.bit, null).assertEqualTo(null)

		branch(10, 20).branchAt(zero.bit).assertEqualTo(10)
		branch(10, 20).branchAt(one.bit).assertEqualTo(20)

		emptyBranch.branchPut(zero.bit, 10).branchAt(zero.bit).assertEqualTo(10)
		emptyBranch.branchPut(zero.bit, 10).branchAt(one.bit).assertEqualTo(null)

		emptyBranch.branchPut(one.bit, 20).branchAt(zero.bit).assertEqualTo(null)
		emptyBranch.branchPut(one.bit, 20).branchAt(one.bit).assertEqualTo(20)

		branch(10, 20).branchPut(zero.bit, 30).branchAt(zero.bit).assertEqualTo(30)
		branch(10, 20).branchPut(zero.bit, 30).branchAt(one.bit).assertEqualTo(20)

		branch(10, 20).branchPut(one.bit, 40).branchAt(zero.bit).assertEqualTo(10)
		branch(10, 20).branchPut(one.bit, 40).branchAt(one.bit).assertEqualTo(40)
	}
}