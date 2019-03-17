package leo32

import leo.base.assertEqualTo
import kotlin.test.Test

class VmTest {
	@Test
	fun vm() {
		val vm = newVm
		newVmCapacity.assertEqualTo(65536)

		vm.top.assertEqualTo(0)
		vm.mem.size.assertEqualTo(65536)

		vm.alloc(65535) {}
		vm.top.assertEqualTo(65535)
		vm.mem.size.assertEqualTo(65536)

		vm.alloc(1) {}
		vm.top.assertEqualTo(65536)
		vm.mem.size.assertEqualTo(131072)
	}
}