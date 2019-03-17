package leo32

import leo.base.appendableString
import leo.base.assertEqualTo
import kotlin.test.Test

class DictTest {
	private val vm = newVm

	private val dict = vm.dictInner(
		mask = 0x4,
		at0 = vm.dictInner(
			mask = 0x2,
			at0 = vm.dictLeaf(value = 1),
			at1 = vm.dictLeaf(value = 2)),
		at1 = vm.dictInner(
			mask = 0x1,
			at0 = vm.dictLeaf(value = 3),
			at1 = vm.dictLeaf(value = 4)))

	@Test
	fun string() {
		appendableString {
			it.appendDict(vm, dict, Appendable::appendT)
		}.assertEqualTo("dict inner mask 0x4 ..at0 dict inner mask 0x2 ..at0 dict leaf 1 ....at1 dict leaf 2 .......at1 dict inner mask 0x1 ..at0 dict leaf 3 ....at1 dict leaf 4 .........")
	}

	@Test
	fun at() {
		vm.dictAt(dict, 0x00).assertEqualTo(1)
		vm.dictAt(dict, 0x01).assertEqualTo(1)
		vm.dictAt(dict, 0x02).assertEqualTo(2)
		vm.dictAt(dict, 0x03).assertEqualTo(2)
		vm.dictAt(dict, 0x04).assertEqualTo(3)
		vm.dictAt(dict, 0x05).assertEqualTo(4)
		vm.dictAt(dict, 0x06).assertEqualTo(3)
		vm.dictAt(dict, 0x07).assertEqualTo(4)
		vm.dictAt(dict, 0x08).assertEqualTo(1)
		vm.dictAt(dict, 0x09).assertEqualTo(1)
		vm.dictAt(dict, 0x0A).assertEqualTo(2)
		vm.dictAt(dict, 0x0B).assertEqualTo(2)
		vm.dictAt(dict, 0x0C).assertEqualTo(3)
		vm.dictAt(dict, 0x0D).assertEqualTo(4)
		vm.dictAt(dict, 0x0E).assertEqualTo(3)
		vm.dictAt(dict, 0x0F).assertEqualTo(4)
	}
}