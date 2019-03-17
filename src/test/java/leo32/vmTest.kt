package leo32

import leo.base.assertEqualTo
import kotlin.test.Test

class VmTest {
	@Test
	fun vm() {
		val vm = newVm
		vm.top.assertEqualTo(0)

		vm.alloc(3) { top ->
			top.assertEqualTo(3)
			vm.set(top, 0, 1)
			vm.set(top, 1, 2)
			vm.set(top, 2, 3)
		}

		vm.top.assertEqualTo(3)
		vm.get(vm.top).assertEqualTo(1)
		vm.get(vm.top, 1).assertEqualTo(2)
		vm.get(vm.top, 2).assertEqualTo(3)

		vm.alloc(1) { top ->
			top.assertEqualTo(4)
			vm.set(top, 0, 4)
		}

		vm.top.assertEqualTo(4)
		vm.get(vm.top).assertEqualTo(4)
		vm.get(vm.top, 1).assertEqualTo(1)
		vm.get(vm.top, 2).assertEqualTo(2)
		vm.get(vm.top, 3).assertEqualTo(3)

		vm.alloc(1 shl 24) { top ->
			top.assertEqualTo(4 + (1 shl 24))
		}
		vm.top.assertEqualTo(4 + (1 shl 24))
	}
}