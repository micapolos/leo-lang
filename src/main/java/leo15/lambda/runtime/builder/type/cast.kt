package leo15.lambda.runtime.builder.type

import leo13.firstIndexed
import leo13.onlyOrNull
import leo13.size
import leo15.lambda.runtime.builder.Term
import leo15.lambda.runtime.builder.choiceTerm

data class Cast<V>(val termOrNull: Term<V>?)

fun <V, T> Typed<V, T>.castTo(rhs: Type<T>): Typed<V, T>? =
	cast(type, term, rhs, null)?.let { cast ->
		cast.termOrNull?.of(rhs) ?: this
	}

fun <V, T> cast(lhs: Type<T>, term: Term<V>, rhs: Type<T>, recursiveOrNull: Recursive<T>?): Cast<V>? =
	when (rhs) {
		is ValueType ->
			if (lhs is ValueType && lhs.value == rhs.value) Cast(null)
			else null
		is StructType ->
			if (lhs is StructType) cast(lhs.struct, term, rhs.struct, recursiveOrNull)
			else null
		is ChoiceType ->
			cast(lhs, term, rhs.choice)
		is ArrowType ->
			if (lhs is ArrowType) cast(lhs.arrow, rhs.arrow)
			else null
		is RecursiveType ->
			if (lhs is RecursiveType)
				if (lhs.recursive == rhs.recursive) Cast<V>(null)
				else null
			else
				if (lhs == rhs.recursive.type) Cast<V>(null)
				else null
		is RecurseType ->
			if (lhs is RecurseType) Cast(null)
			else if (lhs == recursiveOrNull!!.type) Cast(null)
			else null
	}

fun <V, T> cast(lhs: Struct<T>, term: Term<V>, rhs: Struct<T>, recursiveOrNull: Recursive<T>?): Cast<V>? =
	if (lhs.fieldStack.onlyOrNull != null && rhs.fieldStack.onlyOrNull != null)
		cast(lhs.fieldStack.onlyOrNull!!, term, rhs.fieldStack.onlyOrNull!!, recursiveOrNull)
	else
		if (lhs == rhs) Cast(null)
		else null

fun <V, T> cast(lhs: Field<T>, term: Term<V>, rhs: Field<T>, recursiveOrNull: Recursive<T>?): Cast<V>? =
	if (lhs.name == rhs.name) cast(lhs.rhsType, term, rhs.rhsType, recursiveOrNull)
	else null

fun <V, T> cast(lhs: Type<T>, term: Term<V>, rhs: Choice<T>): Cast<V>? =
	when (lhs) {
		is ValueType -> null
		is StructType -> lhs.struct.fieldStack.onlyOrNull?.let { cast(it, term, rhs) }
		is ChoiceType -> if (lhs.choice == rhs) Cast(null) else null
		is ArrowType -> null
		is RecursiveType -> null
		is RecurseType -> null
	}

fun <V, T> cast(lhs: Arrow<T>, rhs: Arrow<T>): Cast<V>? =
	if (lhs == rhs) Cast(null)
	else null

fun <V, T> cast(lhs: Field<T>, term: Term<V>, rhs: Choice<T>): Cast<V>? =
	rhs.fieldStack.firstIndexed { lhs == this }?.let { indexed ->
		rhs.fieldStack.size.let { size ->
			Cast(choiceTerm(size, indexed.index, term))
		}
	}