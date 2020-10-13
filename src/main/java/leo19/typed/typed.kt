package leo19.typed

import leo.base.failIfOr
import leo.base.fold
import leo13.array
import leo13.map
import leo14.indentString
import leo14.lineTo
import leo14.plus
import leo19.term.Term
import leo19.term.function
import leo19.term.get
import leo19.term.invoke
import leo19.term.nullTerm
import leo19.term.plus
import leo19.term.reflectScript
import leo19.term.term
import leo19.type.ArrowType
import leo19.type.Type
import leo19.type.choiceOrNull
import leo19.type.contentOrNull
import leo19.type.fieldTo
import leo19.type.isComplex
import leo19.type.isSimple
import leo19.type.isStatic
import leo19.type.plus
import leo19.type.reflectScript
import leo19.type.struct
import leo19.type.structOrNull

data class Typed(val term: Term, val type: Type) {
	override fun toString() = reflectScript.indentString
}

val Typed.reflectScript get() = term.reflectScript.plus("of" lineTo type.reflectScript)

infix fun Term.of(type: Type) = Typed(this, type)

val nullTyped = Typed(nullTerm, struct())

fun typed(vararg fields: TypedField): Typed =
	nullTyped.fold(fields) { plus(it) }

fun typed(name: String) = typed(name fieldTo typed())

fun Typed.plus(field: TypedField): Typed =
	type.structOrNull!!.let { struct ->
		if (struct.isStatic)
			if (field.typed.type.isStatic) nullTerm
			else field.typed.term
		else
			if (field.typed.type.isStatic) term
			else term.plus(field.typed.term)
	} of type.plus(field.typeField)

fun Typed.getOrNull(name: String): Typed? =
	type.contentOrNull?.structOrNull?.let { struct ->
		type.indexedOrNull(name)?.let { indexedField ->
			when {
				struct.isStatic -> nullTerm
				!struct.isComplex -> if (indexedField.value.isStatic) nullTerm else term
				else -> term.get(term(indexedField.index))
			}.of(indexedField.value)
		}
	}

fun Typed.make(name: String): Typed =
	term.of(struct(name fieldTo type))

fun Typed.invoke(typed: Typed): Typed =
	(type as ArrowType).arrow.let { arrow ->
		failIfOr(arrow.lhs != typed.type) {
			term.invoke(typed.term).of(arrow.rhs)
		}
	}

val Typed.content: Typed
	get() =
		term of type.contentOrNull!!

fun Typed.invoke(switch: TypedSwitch): Typed =
	type.contentOrNull!!.choiceOrNull!!.let { choice ->
		choice.isSimple.let { isSimple ->
			term(*switch.termStack.map { term(function(this)) }.array)
				.get(if (isSimple) term else term.get(term(0)))
				.of(switch.typeOrNull!!)
		}
	}
