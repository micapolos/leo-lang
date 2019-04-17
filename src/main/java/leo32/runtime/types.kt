package leo32.runtime

import leo.base.Seq
import leo.base.empty
import leo.base.fold
import leo.base.orIfNull
import leo.binary.Bit
import leo32.base.Tree
import leo32.base.at
import leo32.base.tree

data class Types(
	val typeTree: Tree<Type?>)

val Tree<Type?>.types get() =
	Types(this)

fun Types.at(bit: Bit) =
	typeTree.at(bit).orIfNull { empty.tree() }.types

fun Types.at(field: TermField): Types =
	at(field.bitSeq)

fun Types.at(bitSeq: Seq<Bit>): Types =
	fold(bitSeq) { at(it) }

fun Types.plus(field: TermField): Types? =
	typeTree.at(field.bitSeq)?.types
