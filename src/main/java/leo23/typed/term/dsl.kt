package leo23.typed.term

import kotlinx.collections.immutable.toPersistentList
import leo23.term.Expr
import leo23.term.IndexedTerm
import leo23.term.expr
import leo23.term.nilExpr
import leo23.term.tuple
import leo23.term.type.TupleType
import leo23.term.type.does
import leo23.type.ArrowType
import leo23.type.BooleanType
import leo23.type.ChoiceType
import leo23.type.NumberType
import leo23.type.StructType
import leo23.type.TextType
import leo23.type.Type
import leo23.type.booleanType
import leo23.type.numberType
import leo23.type.textType
import leo23.typed.Typed
import leo23.typed.of

typealias Compiled = Typed<Expr, Type>

sealed class Case
data class CompiledCase(val compiled: Compiled) : Case()
data class TypeCase(val type: Type) : Case()

val Case.type: Type
	get() =
		when (this) {
			is CompiledCase -> compiled.t
			is TypeCase -> type
		}

fun compiled(boolean: Boolean): Compiled = expr(boolean).of(booleanType)
fun compiled(string: String): Compiled = expr(string).of(textType)
fun compiled(int: Int): Compiled = expr(int).of(numberType)
fun compiled(double: Double): Compiled = expr(double).of(numberType)

fun <T> with(vararg fields: T) = listOf(*fields)

val String.structCompiled get() = this struct with()
infix fun String.struct(fields: List<Typed<Expr, Type>>): Typed<Expr, Type> =
	when (fields.size) {
		0 -> nilExpr
		1 -> fields[0].v
		else -> tuple(*fields.map { it.v }.toTypedArray())
	}.of(StructType(this, fields.map { it.t }.toPersistentList()))

fun case(compiled: Compiled): Case = CompiledCase(compiled)
fun case(type: Type): Case = TypeCase(type)

val List<Case>.indexedTerm: IndexedTerm
	get() =
		indexOfLast { it is CompiledCase }.let { index ->
			IndexedTerm(index, (get(index) as CompiledCase).compiled.v)
		}

infix fun String.choice(cases: List<Case>): Typed<Expr, Type> =
	Expr(
		cases.indexedTerm,
		leo23.term.type.ChoiceType(cases.map { it.type.termType }))
		.of(ChoiceType(this, cases.map { it.type }.toPersistentList()))

val Type.termType: leo23.term.type.Type
	get() =
		when (this) {
			BooleanType -> leo23.term.type.BooleanType
			TextType -> leo23.term.type.TextType
			NumberType -> leo23.term.type.NumberType
			is ArrowType -> paramTypes.map { it.termType }.does(returnType.termType)
			is StructType -> when (fields.size) {
				0 -> leo23.term.type.NilType
				1 -> fields[0].termType
				else -> TupleType(fields.map { it.termType })
			}
			is ChoiceType -> leo23.term.type.ChoiceType(cases.map { it.termType })
		}
