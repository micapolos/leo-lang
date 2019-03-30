package leo32.base

import leo.base.appendableString
import leo.base.fold
import leo.base.indexed
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import leo32.ptr.Ptr
import leo32.ptr.nullPtr
import leo32.ptr.ptrBranchAt
import leo32.ptr.ptrBranchPut

data class Array<out T>(
	val hsbMask: Int,
	val default: T,
	val branchPtr: Ptr) {
	override fun toString() = appendableString { it.append(this) }
}

val <T> T.array
	get() =
		Array(1 shl 31, this, nullPtr)

fun <T> T.arrayOfIndexed(vararg pairs: IndexedValue<T>) =
	array.fold(pairs) { put(it.index, it.value) }

fun <T> T.arrayOf(vararg values: T) =
	values.foldIndexed(array) { index, array, value ->
		array.put(index, value)
	}

val <T> T.array8
	get() =
		Array(1 shl 7, this, nullPtr)

fun <T> Array<T>.at(index: Int): T =
	ptrValue(branchPtr.arrayAt(index, hsbMask))

fun <T> Array<T>.put(index: Int, value: T): Array<T> =
	copy(branchPtr = branchPtr.arrayPut(index, hsbMask, if (value == default) null else value))

fun <T> Array<T>.update(index: Int, fn: T.() -> T): Array<T> =
	put(index, at(index).fn())

fun <T, R> R.foldIndexed(array: Array<T>, fn: R.(IndexedValue<T>) -> R): R =
	foldArrayIndexed(array.branchPtr, 0, array.hsbMask) {
		fn(it.index indexed array.ptrValue(it.value))
	}

fun <T, R> R.fold(array: Array<T>, fn: R.(T) -> R): R =
	foldArray(array.branchPtr, array.hsbMask) {
		fn(array.ptrValue(it))
	}

@Suppress("UNCHECKED_CAST")
fun <T> Array<T>.ptrValue(ptr: Ptr) =
	(ptr as T?) ?: default

fun <T> Appendable.append(array: Array<T>): Appendable =
	this
		.append("${array.default}.array")
		.foldIndexed(array) {
			append(".put(${it.index}, ${it.value})")
		}

// === unchecked ===

fun Ptr.arrayAt(index: Int, mask: Int): Ptr =
	if (mask == 0) this
	else ptrBranchAt(index.and(mask).bit).arrayAt(index, mask ushr 1)

fun Ptr.arrayPut(index: Int, mask: Int, value: Ptr): Ptr =
	if (mask == 0) value
	else ptrBranchPut(index.and(mask).bit, ptrBranchAt(index.and(mask).bit).arrayPut(index, mask ushr 1, value))

fun <R> R.foldArrayIndexed(array: Ptr, index: Int, mask: Int, fn: R.(IndexedValue<Ptr>) -> R): R =
	if (array == null) this
	else if (mask == 0) fn(index indexed array)
	else this
		.foldArrayIndexed(array.ptrBranchAt(zero.bit), index, mask ushr 1, fn)
		.foldArrayIndexed(array.ptrBranchAt(one.bit), index or mask, mask ushr 1, fn)

fun <R> R.foldArray(array: Ptr, mask: Int, fn: R.(Ptr) -> R): R =
	if (array == null) this
	else if (mask == 0) fn(array)
	else this
		.foldArray(array.ptrBranchAt(zero.bit), mask ushr 1, fn)
		.foldArray(array.ptrBranchAt(one.bit), mask ushr 1, fn)
