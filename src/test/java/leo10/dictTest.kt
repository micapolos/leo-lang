package leo10

import leo.base.assertEqualTo
import leo.base.empty
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class DictTest {
	@Test
	fun setBit() {
		dict<String>(empty)
			.apply { at(bit0).assertEqualTo(dict(empty)) }
			.apply { at(bit1).assertEqualTo(dict(empty)) }

		dict<String>(empty)
			.set(bit0, dict(link(leaf("0"))))
			.apply { at(bit0).assertEqualTo(dict(link(leaf("0")))) }
			.apply { at(bit1).assertEqualTo(dict(empty)) }

		dict<String>(empty)
			.set(bit0, dict(link(leaf("0"))))
			.set(bit1, dict(link(leaf("1"))))
			.apply { at(bit0).assertEqualTo(dict(link(leaf("0")))) }
			.apply { at(bit1).assertEqualTo(dict(link(leaf("1")))) }

		dict<String>(empty)
			.set(bit0, dict(link(leaf("0"))))
			.set(bit0, dict(link(leaf("00"))))
			.apply { at(bit0).assertEqualTo(dict(link(leaf("00")))) }
			.apply { at(bit1).assertEqualTo(dict(empty)) }

		dict<String>(empty)
			.set(bit0, dict(link(leaf("0"))))
			.set(bit1, dict(link(leaf("1"))))
			.set(bit0, dict(empty))
			.apply { at(bit0).assertEqualTo(dict(empty)) }
			.apply { at(bit1).assertEqualTo(dict(link(leaf("1")))) }

		dict<String>(empty)
			.set(bit0, dict(link(leaf("0"))))
			.set(bit1, dict(link(leaf("1"))))
			.set(bit0, dict(empty))
			.set(bit1, dict(empty))
			.apply { at(bit0).assertEqualTo(dict(empty)) }
			.apply { at(bit1).assertEqualTo(dict(empty)) }
	}

	@Test
	fun mutation() {
		dict<Int>(empty)
			.at("one")
			.assertEqualTo(null)

		dict<Int>(empty)
			.set("one", 1)
			.apply { at("one").assertEqualTo(1) }
			.apply { at("two").assertEqualTo(null) }
			.apply { at("on").assertEqualTo(null) }
			.apply { at("onee").assertEqualTo(null) }

		dict<Int>(empty)
			.set("one", 1)
			.set("one", 11)
			.apply { at("one").assertEqualTo(11) }
			.apply { at("two").assertEqualTo(null) }

		dict<Int>(empty)
			.set("one", 1)
			.set("two", 2)
			.apply { at("one").assertEqualTo(1) }
			.apply { at("two").assertEqualTo(2) }
			.apply { at("three").assertEqualTo(null) }
	}
}