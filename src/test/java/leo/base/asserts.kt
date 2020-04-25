package leo.base

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

fun <V> V.assertReturns() {}

fun <V> V.assertEqualTo(other: V, message: String? = null) =
	assertEquals(other, this, message)

infix fun <V> V.assertEqualTo(other: V) =
	assertEqualTo(other, null)

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

fun assertTimesOutMillis(timeoutMillis: Long, fn: () -> Unit) {
	val thread = Thread(fn)
	thread.start()
	thread.join(timeoutMillis)
	if (thread.isAlive) thread.interrupt()
	else throw AssertionError("Expected to timeout")
}

fun assertStackOverflows(fn: () -> Unit) =
	assertFailsWith(StackOverflowError::class, fn)
