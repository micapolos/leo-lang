package leo16.compiler

import leo13.asStack
import leo13.isEmpty
import leo13.map
import leo16.Field
import leo16.Value
import leo16.emptyValue
import leo16.invoke
import leo16.names.*
import leo16.plus
import leo16.value

val Type.value: Value
	get() =
		choice.value

val TypeChoice.value: Value
	get() =
		if (caseStackLink.stack.isEmpty) caseStackLink.value.value
		else _choice.invoke(casesValue).value

val TypeChoice.casesValue: Value
	get() =
		caseStackLink.asStack.map { valueField }.value

val TypeCase.valueField: Field
	get() =
		_case(value)

val TypeCase.value: Value
	get() =
		when (this) {
			EmptyTypeCase -> emptyValue
			is LinkTypeCase -> link.type.value.plus(link.field.field)
		}

val TypeField.field: Field
	get() =
		when (this) {
			is SentenceTypeField -> sentence.valueField
			is FunctionTypeField -> function.valueField
		}

val TypeSentence.valueField: Field
	get() =
		word(type.value)

val TypeFunction.valueField: Field
	get() =
		_function.invoke(emptyValue)