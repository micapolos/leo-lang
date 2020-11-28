package leo23.typed.term

import kotlinx.collections.immutable.toPersistentList
import leo13.Stack
import leo13.array
import leo13.onlyOrNull
import leo13.push
import leo13.size
import leo13.stack
import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.number
import leo23.term.Expr
import leo23.term.IndexedTerm
import leo23.term.expr
import leo23.term.nilExpr
import leo23.term.numberEquals
import leo23.term.numberMinus
import leo23.term.numberPlus
import leo23.term.numberText
import leo23.term.numberTimes
import leo23.term.textAppend
import leo23.term.textEquals
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
import leo23.type.fields
import leo23.type.numberType
import leo23.type.struct
import leo23.type.textType
import leo23.typed.Typed
import leo23.typed.of

typealias Compiled = Typed<Expr, Type>
typealias StackCompiled = Typed<Stack<Expr>, Stack<Type>>

val emptyStackCompiled: StackCompiled get() = stack<Expr>().of(stack())

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
fun compiled(number: Number): Compiled = expr(number).of(numberType)
fun compiled(int: Int): Compiled = compiled(int.number)
fun compiled(double: Double): Compiled = compiled(double.number)
fun compiled(literal: Literal): Compiled = when (literal) {
	is StringLiteral -> compiled(literal.string)
	is NumberLiteral -> compiled(literal.number)
}

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

fun Compiled.numberPlus(rhs: Compiled): Compiled = v.numberPlus(rhs.v).of(numberType)
fun Compiled.numberMinus(rhs: Compiled): Compiled = v.numberMinus(rhs.v).of(numberType)
fun Compiled.numberTimes(rhs: Compiled): Compiled = v.numberTimes(rhs.v).of(numberType)
fun Compiled.numberEquals(rhs: Compiled): Compiled = v.numberEquals(rhs.v).of(booleanType)
val Compiled.numberText: Compiled get() = v.numberText.of(textType)

fun Compiled.textAppend(rhs: Compiled): Compiled = v.textAppend(rhs.v).of(textType)
fun Compiled.textEquals(rhs: Compiled): Compiled = v.textEquals(rhs.v).of(booleanType)

fun StackCompiled.push(compiled: Compiled): StackCompiled = v.push(compiled.v).of(t.push(compiled.t))
val Compiled.stackCompiled: StackCompiled get() = stack(v).of(stack(t))
infix fun String.struct(stackCompiled: StackCompiled): Compiled =
	when (stackCompiled.t.size) {
		0 -> nilExpr
		1 -> stackCompiled.v.onlyOrNull!!
		else -> tuple(*stackCompiled.v.array)
	}.of(this struct fields(*stackCompiled.t.array))
