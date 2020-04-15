package leo15

import leo.base.indexed
import leo.base.notNullIf
import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.lambda.*

sealed class Cast
object IdentityCast : Cast()
data class TermCast(val term: Term) : Cast()

val identityCast: Cast = IdentityCast
val Term.cast: Cast get() = TermCast(this)

fun Cast.term(input: Term): Term =
	when (this) {
		IdentityCast -> input
		is TermCast -> term
	}

fun Typed.castTo(to: Type): Typed? =
	term.cast(type, to, null)?.let { cast ->
		when (cast) {
			IdentityCast -> to.typed(term)
			is TermCast -> to.typed(cast.term)
		}
	}

fun Typed.castTerm(to: Type): Term? =
	term.cast(type, to, null)?.term(term)

fun Term.cast(from: Type, to: Type, recursive: TypeRecursive?): Cast? =
	if (from !is AlternativeType && to is AlternativeType) cast(from, to.alternative, 0, recursive)
	else if (from !is RepeatingType && to is RepeatingType) cast(from, to.repeating, recursive)
	else when (from) {
		EmptyType -> castEmpty(to)
		AnythingType -> castAnything(to)
		NothingType -> castNothing(to)
		is LinkType -> cast(from.link, to, recursive)
		is AlternativeType -> cast(from.alternative, to)
		is FunctionType -> cast(from.function, to)
		is RepeatingType -> cast(from.repeating, to)
		is RecursiveType -> cast(from.recursive, to)
		RecurseType -> castRecurse(to, recursive)
	}

fun castEmpty(to: Type): Cast? =
	if (to is EmptyType) identityCast
	else null

fun castAnything(to: Type): Cast? =
	if (to is AnythingType) identityCast
	else null

fun castNothing(to: Type): Cast? =
	if (to is NothingType) identityCast
	else null

fun Term.cast(from: TypeLink, to: Type, recursive: TypeRecursive?): Cast? =
	if (to is LinkType)
		if (from.lhs.isEmpty)
			if (to.link.lhs.isEmpty) cast(from.line, to.link.line, recursive)
			else null
		else invoke(firstTerm).let { lhsTerm ->
			invoke(secondTerm).let { rhsTerm ->
				lhsTerm.cast(from.lhs, to.link.lhs, recursive)?.let { lhsCast ->
					rhsTerm.cast(from.line, to.link.line, recursive)?.let { rhsCast ->
						when (lhsCast) {
							IdentityCast ->
								when (rhsCast) {
									IdentityCast -> identityCast
									is TermCast -> lhsTerm.plus(rhsCast.term).cast
								}
							is TermCast ->
								when (rhsCast) {
									IdentityCast -> lhsCast.term.plus(rhsTerm).cast
									is TermCast -> lhsCast.term.plus(rhsCast.term).cast
								}
						}
					}
				}
			}
		}
	else null

fun cast(from: TypeAlternative, to: Type): Cast? =
	if (to is AlternativeType) cast(from, to.alternative)
	else null

fun Term.cast(from: Type, to: Type, alternativeIndex: Int, recursive: TypeRecursive?): Cast? =
	cast(from, to, recursive)?.let { cast ->
		(alternativeIndex indexed cast.term(this)).valueTerm.cast
	}

fun Term.cast(from: Type, to: TypeAlternative, alternativeIndex: Int, recursive: TypeRecursive?): Cast? =
	cast(from, to.rhs, alternativeIndex, recursive) ?: cast(from, to.lhs, alternativeIndex.inc(), recursive)

fun cast(from: TypeAlternative, to: TypeAlternative): Cast? =
	if (from == to) identityCast
	else null

fun cast(from: TypeFunction, to: Type): Cast? =
	if (to is FunctionType) cast(from, to.function)
	else null

// TODO: implement widening and narrowing
fun cast(from: TypeFunction, to: TypeFunction): Cast? =
	if (from == to) identityCast
	else null

fun Term.cast(from: Type, to: TypeRepeating, recursive: TypeRecursive?): Cast? =
	if (from is RepeatingType) notNullIf(from.repeating == to) { identityCast }
	else if (from is EmptyType) identityCast
	else if (from is LinkType) cast(from.link, to, recursive)
	else null

fun Term.cast(from: TypeLink, to: TypeRepeating, recursive: TypeRecursive?): Cast? =
	if (from.lhs.isEmpty)
		cast(from.line, to.line, recursive)?.let { cast ->
			nilTerm.plusRepeating(cast.term(this)).cast
		}
	else invoke(firstTerm).let { lhsTerm ->
		invoke(secondTerm).let { rhsTerm ->
			lhsTerm.cast(from.lhs, to, recursive)?.let { lhsCast ->
				rhsTerm.cast(from.line, to.line, recursive)?.let { rhsCast ->
					lhsCast.term(lhsTerm).plusRepeating(rhsCast.term(rhsTerm)).cast
				}
			}
		}
	}

fun cast(from: TypeRepeating, to: Type): Cast? =
	if (to is RepeatingType) notNullIf(from == to.repeating) { identityCast }
	else null

fun Term.cast(from: TypeRecursive, to: Type): Cast? =
	if (to is RecursiveType) cast(from.type, to.recursive.type, to.recursive)
	else cast(from.type, to, to.recursive)

fun Term.castRecurse(to: Type, recursive: TypeRecursive?): Cast? =
	if (to is RecurseType) identityCast
	else if (recursive == null) null
	else cast(recursive.type, to, recursive)

fun Term.cast(from: TypeLine, to: TypeLine, recursive: TypeRecursive?): Cast? =
	when (from) {
		is LiteralTypeLine -> cast(from.literal, to)
		is FieldTypeLine -> cast(from.field, to, recursive)
		JavaTypeLine -> castJava(to)
	}

fun cast(from: Literal, to: TypeLine): Cast? =
	if (to is LiteralTypeLine) cast(from, to.literal)
	else castPrimitive(from, to)

fun castPrimitive(from: Literal, to: TypeLine): Cast? =
	when (from) {
		is StringLiteral -> cast(from.string, to)
		is NumberLiteral -> cast(from.number, to)
	}

fun cast(from: Literal, to: Literal): Cast? =
	if (from == to) identityCast
	else null

fun cast(from: String, to: TypeLine): Cast? =
	if (to == textTypeLine) from.valueTerm.cast
	else null

fun cast(from: Number, to: TypeLine): Cast? =
	if (to == numberTypeLine) from.bigDecimal.valueTerm.cast
	else null

fun Term.cast(from: TypeField, to: TypeLine, recursive: TypeRecursive?): Cast? =
	if (to is FieldTypeLine) cast(from, to.field, recursive)
	else null

fun Term.cast(from: TypeField, to: TypeField, recursive: TypeRecursive?): Cast? =
	if (from.name == to.name) cast(from.rhs, to.rhs, recursive)
	else null

fun castJava(to: TypeLine): Cast? =
	if (to is JavaTypeLine) identityCast
	else null