@file:Suppress("UNCHECKED_CAST")

package leo32

import leo.base.appendableString
import leo.base.indexed
import leo.binary.bit
import leo.binary.one
import leo.binary.zero

data class Array<T>(
	val default: T,
	val branch: Ptr) {
	override fun toString() = appendableString { it.append(this) }
}

val <T> T.array
	get() =
		Array(this, emptyBranch)

fun <T> Array<T>.at(index: Int): T =
	branch.arrayAt(index, arrayStartMask) as T? ?: default

fun <T> Array<T>.put(index: Int, value: T): Array<T> =
	copy(branch = branch.arrayPut(index, arrayStartMask, if (value == default) null else value))

fun <T> Array<T>.update(index: Int, fn: T.() -> T): Array<T> =
	put(index, at(index).fn())

fun <T, R> R.foldIndexed(array: Array<T>, fn: R.(IndexedValue<T>) -> R): R =
	foldArrayIndexed(array.branch, 0, arrayStartMask) {
		fn(it.index indexed (it.value as T))
	}

fun <T> Appendable.append(array: Array<T>): Appendable =
	this
		.append("${array.default}.array")
		.foldIndexed(array) {
			append(".put(${it.index}, ${it.value})")
		}

// === unchecked ===

val arrayStartMask = 1 shl 31

fun Ptr.arrayAt(index: Int, mask: Int): Ptr =
	if (mask == 0) this
	else branchAt(index.and(mask).bit).arrayAt(index, mask ushr 1)

fun Ptr.arrayPut(index: Int, mask: Int, value: Ptr): Ptr =
	if (mask == 0) value
	else branchPut(index.and(mask).bit, branchAt(index.and(mask).bit).arrayPut(index, mask ushr 1, value))

fun <R> R.foldArrayIndexed(array: Ptr, index: Int, mask: Int, fn: R.(IndexedValue<Ptr>) -> R): R =
	if (array == null) this
	else if (mask == 0) fn(index indexed array)
	else this
		.foldArrayIndexed(array.branchAt(zero.bit), index, mask ushr 1, fn)
		.foldArrayIndexed(array.branchAt(one.bit), index or mask, mask ushr 1, fn)
