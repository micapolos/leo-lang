package leo32.runtime.v2

import leo32.base.List
import leo32.base.add
import leo32.base.list

data class Complex(
	val primitive: Primitive,
	val firstField: Field,
	val remainingFieldList: List<Field>)

fun complex(primitive: Primitive, field: Field, vararg fields: Field) =
	Complex(primitive, field, list(*fields))

fun Primitive.plus(field: Field) =
	complex(this, field)

fun Complex.plus(field: Field) =
	Complex(primitive, firstField, remainingFieldList.add(field))
