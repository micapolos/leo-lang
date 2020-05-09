package leo16

import leo.base.assertEqualTo
import leo15.*
import kotlin.test.Test

class ValueOpsTest {
	@Test
	fun listMatchValue_empty() {
		value(emptyName())
			.listMatchField
			.assertEqualTo(emptyName())
	}

	@Test
	fun listMatchValue_single() {
		value(itemName(zeroName()))
			.listMatchField
			.assertEqualTo(linkedName(previousName(listName(emptyName())), lastName(itemName(zeroName()))))
	}

	@Test
	fun listMatchValue_many() {
		value(itemName(zeroName()), itemName(oneName()))
			.listMatchField
			.assertEqualTo(linkedName(previousName(listName(itemName(zeroName()))), lastName(itemName(oneName()))))
	}
}