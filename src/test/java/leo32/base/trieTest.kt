package leo32.base

import leo.base.assertEqualTo
import kotlin.test.Test

class TrieTest {
	@Test
	fun put() {
		0.trie(0b10)
			.put(0b00, 0b11, 0b10, 128)
			.apply { at(0b00, 0b10).assertEqualTo(128) }
			.apply { at(0b01, 0b10).assertEqualTo(0) }
			.apply { at(0b10, 0b10).assertEqualTo(0) }
			.apply { at(0b11, 0b10).assertEqualTo(0) }

		0.trie(0b10)
			.put(0b00, 0b10, 0b10, 128)
			.apply { at(0b00, 0b10).assertEqualTo(128) }
			.apply { at(0b01, 0b10).assertEqualTo(128) }
			.apply { at(0b10, 0b10).assertEqualTo(0) }
			.apply { at(0b11, 0b10).assertEqualTo(0) }

		0.trie(0b10)
			.put(0b00, 0b01, 0b10, 128)
			.apply { at(0b00, 0b10).assertEqualTo(128) }
			.apply { at(0b01, 0b10).assertEqualTo(0) }
			.apply { at(0b10, 0b10).assertEqualTo(128) }
			.apply { at(0b11, 0b10).assertEqualTo(0) }

		0.trie(0b10)
			.put(0b00, 0b00, 0b10, 128)
			.apply { at(0b00, 0b10).assertEqualTo(128) }
			.apply { at(0b01, 0b10).assertEqualTo(128) }
			.apply { at(0b10, 0b10).assertEqualTo(128) }
			.apply { at(0b11, 0b10).assertEqualTo(128) }

		0.trie(0b10)
			.put(0b00, 0b00, 0b10, 128)
			.put(0b10, 0b11, 0b10, 129)
			.apply { at(0b00, 0b10).assertEqualTo(128) }
			.apply { at(0b01, 0b10).assertEqualTo(128) }
			.apply { at(0b10, 0b10).assertEqualTo(129) }
			.apply { at(0b11, 0b10).assertEqualTo(128) }

		0.trie(0b10)
			.put(0b00, 0b00, 0b10, 128)
			.put(0b00, 0b10, 0b10, 129)
			.apply { at(0b00, 0b10).assertEqualTo(129) }
			.apply { at(0b01, 0b10).assertEqualTo(129) }
			.apply { at(0b10, 0b10).assertEqualTo(128) }
			.apply { at(0b11, 0b10).assertEqualTo(128) }

		0.trie(0b10)
			.put(0b00, 0b00, 0b10, 128)
			.put(0b00, 0b01, 0b10, 129)
			.apply { at(0b00, 0b10).assertEqualTo(129) }
			.apply { at(0b01, 0b10).assertEqualTo(128) }
			.apply { at(0b10, 0b10).assertEqualTo(129) }
			.apply { at(0b11, 0b10).assertEqualTo(128) }

		0.trie(0b10)
			.put(0b00, 0b00, 0b10, 128)
			.put(0b00, 0b00, 0b10, 129)
			.apply { at(0b00, 0b10).assertEqualTo(129) }
			.apply { at(0b01, 0b10).assertEqualTo(129) }
			.apply { at(0b10, 0b10).assertEqualTo(129) }
			.apply { at(0b11, 0b10).assertEqualTo(129) }
	}
}