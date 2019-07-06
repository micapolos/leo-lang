package lambda.lib

import lambda.invoke
import leo.base.assertEqualTo
import kotlin.test.Test

class BranchTest {
	@Test
	fun test() {
		branch0(zeroBit).assertEqualTo(branch0(zeroBit))
		branch0(oneBit).assertEqualTo(branch0(oneBit))
		branch1(zeroBit).assertEqualTo(branch1(zeroBit))
		branch1(oneBit).assertEqualTo(branch1(oneBit))

		branch0(zeroBit).branchSwitch(bitNegate, id).assertEqualTo(oneBit)
		branch0(oneBit).branchSwitch(bitNegate, id).assertEqualTo(zeroBit)
		branch1(zeroBit).branchSwitch(bitNegate, id).assertEqualTo(zeroBit)
		branch1(oneBit).branchSwitch(bitNegate, id).assertEqualTo(oneBit)
	}
}