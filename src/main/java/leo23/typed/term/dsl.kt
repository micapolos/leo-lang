package leo23.typed.term

import kotlinx.collections.immutable.toPersistentList
import leo23.term.Expr
import leo23.term.expr
import leo23.term.nilExpr
import leo23.term.tuple
import leo23.type.StructType
import leo23.type.Type
import leo23.type.booleanType
import leo23.type.numberType
import leo23.type.textType
import leo23.typed.Typed
import leo23.typed.of

typealias Compiled = Typed<Expr, Type>

fun compiled(boolean: Boolean): Compiled = expr(boolean).of(booleanType)
fun compiled(string: String): Compiled = expr(string).of(textType)
fun compiled(int: Int): Compiled = expr(int).of(numberType)
fun compiled(double: Double): Compiled = expr(double).of(numberType)

fun fields(vararg fields: Typed<Expr, Type>) = listOf(*fields)

infix fun String.struct(fields: List<Typed<Expr, Type>>): Typed<Expr, Type> =
	when (fields.size) {
		0 -> nilExpr
		1 -> fields[0].v
		else -> tuple(*fields.map { it.v }.toTypedArray())
	}.of(StructType(this, fields.map { it.t }.toPersistentList()))
