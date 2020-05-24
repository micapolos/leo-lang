package leo16.lambda

import leo13.map
import leo16.Field
import leo16.Value
import leo16.emptyValue
import leo16.field
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
		_type((if (isStatic) _static else _dynamic)(choice.asField))

val TypeChoice.asField: Field
	get() =
		null
			?: onlyCaseOrNull?.asField
			?: _choice(caseStackLink.map { asField }.value)

val TypeCase.asField: Field
	get() =
		_case(asValue)

val TypeCase.asValue: Value
	get() =
		when (this) {
			EmptyTypeCase -> emptyValue
			is LinkTypeCase -> link.type.asField.value.plus(link.field.asField)
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
