package leo.base

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun <V> V.assertReturns() {}

infix fun <V> V.assertEqualTo(other: V) =
	assertEquals(other, this)

infix fun <V> V.assertNotEqualTo(other: V) =
	assertNotEquals(other, this)

val Boolean.assert
	get() =
		assertEqualTo(true)

val <V : Any> V?.assertNotNull
	get() =
		kotlin.test.assertNotNull(this)

val <V : Any> V?.assertNull
	get() =
		kotlin.test.assertNull(this)

fun <V, R> V.assertFails(fn: V.() -> R) =
	kotlin.test.assertFails { fn() }
