package leo32.runtime

import leo32.base.I32
import leo32.base.only

sealed class Get

data class NameGet(
	val name: String): Get()

data class IndexGet(
	val index: I32): Get()

fun getter(name: String) =
	NameGet(name) as Get

fun getter(index: I32) =
	IndexGet(index) as Get

val String.getter get() =
	NameGet(this) as Get

val I32.getter get() =
	IndexGet(this) as Get

fun Get.invoke(term: Term): Term =
	when (this) {
		is NameGet -> term.at(name).only
		is IndexGet -> term.at(index).value
	}
