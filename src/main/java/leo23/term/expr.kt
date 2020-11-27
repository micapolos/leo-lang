package leo23.term

import leo23.term.type.Type

data class Expr(val term: Term, val type: Type)

infix fun Term.of(type: Type) = Expr(this, type)

fun Expr.check(type: Type): Expr =
	also { check(this.type == type) }

fun List<Expr>.check(types: List<Type>): List<Expr> =
	this
		.also { check(size == types.size) }
		.also { zip(types, Expr::check) }