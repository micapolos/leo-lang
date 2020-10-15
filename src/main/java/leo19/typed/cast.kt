package leo19.typed

import leo.base.failIfOr
import leo.base.indexed
import leo.base.mapFirstOrNull
import leo.base.notNullIf
import leo13.array
import leo13.indexed
import leo13.onlyOrNull
import leo13.push
import leo13.reverse
import leo13.seq
import leo13.size
import leo13.stack
import leo13.zipFoldOrNull
import leo19.term.Term
import leo19.term.function
import leo19.term.get
import leo19.term.invoke
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.ArrowType
import leo19.type.Choice
import leo19.type.ChoiceType
import leo19.type.Field
import leo19.type.IntRangeType
import leo19.type.Struct
import leo19.type.StructType
import leo19.type.Type
import leo19.type.arrowOrNull
import leo19.type.isSimple
import leo19.type.structOrNull

fun Typed.castTo(dstType: Type): Typed =
	cast(term, type, dstType).of(dstType)

fun cast(srcTerm: Term, srcType: Type, dstType: Type): Term =
	if (srcType == dstType) srcTerm
	else when (srcType) {
		is StructType ->
			if (dstType is ChoiceType) cast(srcTerm, srcType.struct, dstType.choice)
			else cast(srcTerm, srcType.struct, dstType.structOrNull!!)
		is ChoiceType -> null
		is ArrowType -> cast(srcTerm, srcType.arrow, dstType.arrowOrNull!!)
		is IntRangeType -> TODO()
	}!!

fun cast(srcTerm: Term, srcStruct: Struct, dstChoice: Choice): Term =
	cast(srcTerm, srcStruct.fieldStack.onlyOrNull!!, dstChoice)

fun cast(srcTerm: Term, srcField: Field, dstChoice: Choice): Term =
	dstChoice.caseStack.seq.indexed.mapFirstOrNull {
		notNullIf(value.name == srcField.name) {
			term(dstChoice.caseStack.size.minus(index).dec()).let { indexTerm ->
				if (dstChoice.isSimple) indexTerm
				else term(indexTerm, cast(srcTerm, srcField.type, value.type))
			}
		}
	}!!

fun cast(srcTerm: Term, srcStruct: Struct, dstStruct: Struct): Term =
	stack<Term>()
		.zipFoldOrNull(srcStruct.fieldStack.reverse.indexed.reverse, dstStruct.fieldStack) { indexedSrcField, dstField ->
			push(cast(srcTerm.get(term(indexedSrcField.index)), indexedSrcField.value, dstField))
		}!!
		.let { termStack -> term(*termStack.array) }

fun cast(srcTerm: Term, srcField: Field, dstField: Field): Term =
	failIfOr(srcField.name != dstField.name) {
		cast(srcTerm, srcField.type, dstField.type)
	}

fun cast(srcTerm: Term, srcArrow: Arrow, dstArrow: Arrow): Term =
	cast(term(variable(0)), srcArrow.lhs, dstArrow.lhs).let { lhsTerm ->
		cast(srcTerm.invoke(lhsTerm), dstArrow.rhs, srcArrow.rhs).let { rhsTerm ->
			term(function(rhsTerm))
		}
	}