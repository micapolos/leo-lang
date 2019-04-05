package leo32.base

import leo.base.assertNotNull
import leo.base.iterate
import leo.binary.randomBit
import org.junit.Test

class TreeTest {
	@Test
	fun longCursor() {
		0.leaf.tree.cursor
			.iterate(1000000) { toWithDefault(randomBit) { 0 } }
			.update { 128.leaf.tree }
			.collapse
			.assertNotNull
	}
}