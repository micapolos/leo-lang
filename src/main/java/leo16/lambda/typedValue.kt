package leo16.lambda

import leo15.lambda.choiceTerm
import leo15.lambda.unsafeUnchoice
import leo15.lambda.value
import leo16.Field
import leo16.Value
import leo16.emptyValue
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.plus

val Typed.value: Value
	get() =
		bodyTyped.value

val BodyTyped.value: Value
	get() =
		match(
			{ emptyValue },
			{ it.value },
			{ it.value }
		)

val LinkTyped.value: Value
	get() =
		previousTyped.value.plus(lastFieldTyped.valueField)

val AlternativeTyped.value: Value
	get() =
		term.unsafeUnchoice(2).run {
			if (index == 0) (value of alternative.firstType).value
			else (value of alternative.secondType).value
		}

val FieldTyped.valueField: Field
	get() =
		match(
			{ it.valueField },
			{ it.valueField },
			{ it.valueField }
		)

val SentenceTyped.valueField: Field
	get() =
		sentence.word.invoke(rhsTyped.value)

val FunctionTyped.valueField: Field
	get() =
		_taking(function.input.value.plus(_giving(function.output.value)))

val NativeTyped.valueField: Field
	get() =
		term.value!!.nativeField

fun Typed.or(type: Type): Typed =
	choiceTerm(2, 0, term) of (this.type or type)

fun Type.or(typed: Typed): Typed =
	choiceTerm(2, 1, typed.term) of (this or typed.type)