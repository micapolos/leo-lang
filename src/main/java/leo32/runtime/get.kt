package leo32.runtime

import leo32.base.I32

sealed class Get

data class IndexGet(
	val index: I32): Get()

data class LhsGet(
	val lhs: Lhs): Get()

data class RhsGet(
	val rhs: Rhs): Get()

fun get(index: I32) =
	IndexGet(index) as Get

fun get(lhs: Lhs) =
	LhsGet(lhs) as Get

fun get(rhs: Rhs) =
	RhsGet(rhs) as Get

val I32.get get() =
	IndexGet(this) as Get

fun Get.invoke(term: Term): Term =
	when (this) {
		is IndexGet -> term.fieldAt(index).value
		is LhsGet -> term.nodeOrNull!!.lhs
		is RhsGet -> term.nodeOrNull!!.field.value
	}
