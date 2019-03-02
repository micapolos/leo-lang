package leo.base

import kotlin.test.assertEquals

infix fun <V> V.assertEqualTo(other: V) =
	assertEquals(other, this)

val <V : Any> V?.assertNotNull
	get() =
		kotlin.test.assertNotNull(this)
