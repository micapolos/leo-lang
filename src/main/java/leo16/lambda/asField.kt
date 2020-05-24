package leo16.lambda

import leo16.Field
import leo16.Value
import leo16.emptyValue
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.orNullAsField
import leo16.plus
import leo16.value

val Type?.orNullAsField: Field
	get() =
		orNullAsField(_type) { asField }

val Type.asField: Field
	get() =
		_type(asValue)

val Type.asValue: Value
	get() =
		(if (isStatic) _static else _dynamic)(body.asField).value

val TypeBody.asField: Field
	get() =
		_case(asValue)

val TypeBody.asValue: Value
	get() =
		when (this) {
			EmptyTypeBody -> emptyValue
			is LinkTypeBody -> link.type.asValue.plus(link.field.asField)
			is AlternativeTypeBody -> alternative.asValue
		}

val TypeField.asField: Field
	get() =
		_field(
			when (this) {
				is SentenceTypeField -> sentence.asField
				is FunctionTypeField -> function.asField
				is NativeTypeField -> native.nativeField
			}
		)

val TypeSentence.asField: Field
	get() =
		word(type.asField)

val TypeFunction.asField: Field
	get() =
		_function(
			_input(input.asField),
			_output(output.asField))

val TypeAlternative.asValue: Value
	get() =
		firstType.asValue.plus(_or(secondType.asValue))
