package leo16.lambda

import leo16.Field
import leo16.Value
import leo16.emptyValue
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.plus
import leo16.value

val Type.reflectField: Field
	get() =
		_type(reflectValue)

val Type.reflectValue: Value
	get() =
		(if (isStatic) _static else _dynamic)(body.reflectField).value

val TypeBody.reflectField: Field
	get() =
		_case(reflectValue)

val TypeBody.reflectValue: Value
	get() =
		when (this) {
			EmptyTypeBody -> emptyValue
			is LinkTypeBody -> link.previousType.reflectValue.plus(link.lastField.reflectField)
			is AlternativeTypeBody -> alternative.reflectValue
		}

val TypeField.reflectField: Field
	get() =
		_field(
			when (this) {
				is SentenceTypeField -> sentence.reflectField
				is FunctionTypeField -> function.reflectField
				is NativeTypeField -> native.nativeField
			}
		)

val TypeSentence.reflectField: Field
	get() =
		word(type.reflectField)

val TypeFunction.reflectField: Field
	get() =
		_function(
			_input(input.reflectField),
			_output(output.reflectField))

val TypeAlternative.reflectValue: Value
	get() =
		firstType.reflectValue.plus(_or(secondType.reflectValue))
