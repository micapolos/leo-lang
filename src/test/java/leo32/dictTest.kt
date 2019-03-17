package leo32

import leo.base.assertEqualTo
import kotlin.test.Test

class DictTest {
	@Test
	fun at() {
		val vm = newVm
		val range = 0 until 1.shl(16)
		vm.topPtr.assertEqualTo(0)

		val dict = emptyDict
		vm.topPtr.assertEqualTo(0)
		for (i in range) vm.dictAt(dict, i).assertEqualTo(0)

		var dict2 = dict
		for (i in range) {
			dict2 = vm.dictWith(dict2, i, i)
			vm.topPtr.assertEqualTo((i + 1) * ptrSize * branchSize)
		}
		for (i in range) vm.dictAt(dict, i).assertEqualTo(0)
		for (i in range) vm.dictAt(dict2, i).assertEqualTo(i)
	}
}