package leo23.typed.term

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import leo23.term.Term
import leo23.term.VectorTerm
import leo23.term.nil
import leo23.type.Type
import leo23.type.booleanType
import leo23.type.fields
import leo23.type.numberType
import leo23.type.struct
import leo23.type.textType
import leo23.typed.Typed
import leo23.typed.of

fun boolean(boolean: Boolean) = leo23.term.boolean(boolean).of(booleanType)
fun number(int: Int) = leo23.term.number(int).of(numberType)
fun number(double: Double) = leo23.term.number(double).of(numberType)
fun text(string: String) = leo23.term.text(string).of(textType)

fun fields(vararg fields: Typed<Term, Type>) = persistentListOf(*fields)

infix fun String.struct(fields: PersistentList<Typed<Term, Type>>): Typed<Term, Type> =
	when (fields.size) {
		0 -> nil.of(this struct fields())
		1 -> fields[0].v.of(this struct fields.map { it.t }.toPersistentList())
		else -> VectorTerm(fields.map { it.v }).of(this struct fields.map { it.t }.toPersistentList())
	}
