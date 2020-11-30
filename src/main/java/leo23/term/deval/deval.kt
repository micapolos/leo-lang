package leo23.term.deval

import leo14.Number
import leo23.term.BooleanTerm
import leo23.term.Expr
import leo23.term.IndexedTerm
import leo23.term.NilTerm
import leo23.term.NumberTerm
import leo23.term.StringTerm
import leo23.term.TupleTerm
import leo23.term.of
import leo23.term.type.ArrowType
import leo23.term.type.BooleanType
import leo23.term.type.ChoiceType
import leo23.term.type.NilType
import leo23.term.type.NumberType
import leo23.term.type.TextType
import leo23.term.type.TupleType
import leo23.term.type.Type
import leo23.typed.Typed
import leo23.typed.of
import leo23.value.Value

val Typed<Value, Type>.deval: Expr
	get() =
		when (t) {
			NilType -> NilTerm
			BooleanType -> BooleanTerm(v as Boolean)
			TextType -> StringTerm(v as String)
			NumberType -> NumberTerm(v as Number)
			is TupleType -> TupleTerm((v as List<*>).mapIndexed { index, value -> value!!.of(t.itemTypes[index]).deval })
			is ChoiceType -> IndexedTerm((v as IndexedValue<*>).index, v.value!!.of(t.itemTypes[v.index]).deval)
			is ArrowType -> error("can not deval arrow type")
		}.of(t)