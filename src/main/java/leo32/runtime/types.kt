package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.orIfNull
import leo.binary.Bit
import leo32.Seq32
import leo32.base.*
import leo32.bitSeq

data class Types(
	val typeTree: Tree<Type?>)

val Tree<Type?>.types get() =
	Types(this)

val Empty.types get() =
	tree<Type>().types

fun Types.at(bit: Bit) =
	typeTree.at(bit).orIfNull { empty.tree() }.types

fun Types.at(field: TermField) =
	at32(field.seq32)

fun Types.at32(seq32: Seq32) =
	fold(seq32.bitSeq) { at(it) }

val Types.typeOrNull get() =
	typeTree.leafOrNull?.value

fun Types.put32(pair: Pair<Seq32, Type>) =
	typeTree.put32(pair).types

fun Types.typeTerm(term: Term): Term = TODO()
fun Types.plus(field: TermField): TypeTerms = TODO()
