package leo32.ptr

import leo.base.assertEqualTo
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import leo32.base.Branch
import kotlin.test.Test

class SwitchTest {
	@Test
	fun branch() {
		branchPtr(null, null).assertEqualTo(null)
		branchPtr(0, null).assertEqualTo(Branch(0, null))
		branchPtr(null, 1).assertEqualTo(Branch(null, 1))
		branchPtr(0, 1).assertEqualTo(Branch(0, 1))

		nullPtr.ptrBranchAt(zero.bit).assertEqualTo(null)
		nullPtr.ptrBranchAt(one.bit).assertEqualTo(null)

		branchPtr(10, null).ptrBranchPut(zero.bit, null).assertEqualTo(null)
		branchPtr(10, null).ptrBranchPut(one.bit, null).assertEqualTo(leo32.base.branch(10, null))

		branchPtr(null, 20).ptrBranchPut(zero.bit, null).assertEqualTo(leo32.base.branch(null, 20))
		branchPtr(null, 20).ptrBranchPut(one.bit, null).assertEqualTo(null)

		branchPtr(10, 20).ptrBranchAt(zero.bit).assertEqualTo(10)
		branchPtr(10, 20).ptrBranchAt(one.bit).assertEqualTo(20)

		nullPtr.ptrBranchPut(zero.bit, 10).ptrBranchAt(zero.bit).assertEqualTo(10)
		nullPtr.ptrBranchPut(zero.bit, 10).ptrBranchAt(one.bit).assertEqualTo(null)

		nullPtr.ptrBranchPut(one.bit, 20).ptrBranchAt(zero.bit).assertEqualTo(null)
		nullPtr.ptrBranchPut(one.bit, 20).ptrBranchAt(one.bit).assertEqualTo(20)

		branchPtr(10, 20).ptrBranchPut(zero.bit, 30).ptrBranchAt(zero.bit).assertEqualTo(30)
		branchPtr(10, 20).ptrBranchPut(zero.bit, 30).ptrBranchAt(one.bit).assertEqualTo(20)

		branchPtr(10, 20).ptrBranchPut(one.bit, 40).ptrBranchAt(zero.bit).assertEqualTo(10)
		branchPtr(10, 20).ptrBranchPut(one.bit, 40).ptrBranchAt(one.bit).assertEqualTo(40)
	}
}