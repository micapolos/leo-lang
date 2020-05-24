package leo16.lambda

import leo16.Field
import leo16.Value
import leo16.emptyValue
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.plus

val Type.value: Value
	get() =
		body.value

val TypeBody.value
	get() =
		when (this) {
			EmptyTypeBody -> emptyValue
			is LinkTypeBody -> link.value
			is AlternativeTypeBody -> alternative.value
		}

val TypeLink.value
	get() =
		previousType.value.plus(lastField.valueField)

val TypeAlternative.value
	get() =
		firstType.value.plus(_or(secondType.value))

val TypeField.valueField: Field
	get() =
		when (this) {
			is SentenceTypeField -> sentence.valueField
			is FunctionTypeField -> function.valueField
			is NativeTypeField -> native.nativeField
		}

val TypeSentence.valueField
	get() =
		word(type.value)

val TypeFunction.valueField
	get() =
		_taking(input.value.plus(_giving(output.value)))
