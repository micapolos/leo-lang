package leo32.base

import leo.binary.Bit
import leo.binary.isZero

data class Branch<T>(
	val at0: T,
	val at1: T)

fun <T> branch(at0: T, at1: T) =
	Branch(at0, at1)

fun <T> Branch<T>.at(bit: Bit) =
	if (bit.isZero) at0 else at1

fun <T> Branch<T>.put(bit: Bit, value: T) =
	if (bit.isZero) branch(value, at1)
	else branch(at0, value)

fun <T : Any> branchOrNull(at0: T?, at1: T?): Branch<T?>? =
	if (at0 == null && at1 == null) null
	else Branch(at0, at1)

fun <T : Any> branchOrNull(bit: Bit, value: T?): Branch<T?>? =
	if (bit.isZero) branchOrNull(value, null)
	else branchOrNull(null as T?, value)

fun <T : Any> Branch<T?>.putOrNull(bit: Bit, value: T?): Branch<T?>? =
	if (bit.isZero) branchOrNull(value, at1)
	else branchOrNull(at0, value)
