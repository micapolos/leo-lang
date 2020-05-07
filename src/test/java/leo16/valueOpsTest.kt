package leo16

import leo.base.assertEqualTo
import leo15.*
import kotlin.test.Test

class ValueOpsTest {
	@Test
	fun listMatchValue_empty() {
		value(emptyName())
			.listMatchValue
			.assertEqualTo(value(emptyName()))
	}

	@Test
	fun listMatchValue_single() {
		value(itemName(zeroName()))
			.listMatchValue
			.assertEqualTo(value(linkedName(previousName(listName(emptyName())), lastName(itemName(zeroName())))))
	}

	@Test
	fun listMatchValue_many() {
		value(itemName(zeroName()), itemName(oneName()))
			.listMatchValue
			.assertEqualTo(value(linkedName(previousName(listName(itemName(zeroName()))), lastName(itemName(oneName())))))
	}
}