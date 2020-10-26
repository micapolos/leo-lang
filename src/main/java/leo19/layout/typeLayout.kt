package leo19.layout

import leo.base.runIf
import leo13.push
import leo19.type.ArrowType
import leo19.type.ChoiceType
import leo19.type.Field
import leo19.type.RecurseType
import leo19.type.RecursiveType
import leo19.type.StructType
import leo19.type.Type

val Type.layout: Layout
	get() =
		when (this) {
			is StructType -> TODO()
			is ChoiceType -> TODO()
			is ArrowType -> TODO()
			is RecursiveType -> TODO()
			is RecurseType -> TODO()
		}

fun Field.layoutField(index: Int): LayoutField =
	LayoutField(index, name, type.layout)

fun StructLayoutBody.plus(field: Field): StructLayoutBody =
	field.layoutField(size).let { layoutField ->
		StructLayoutBody(
			size.runIf(layoutField.value.isDynamic) { inc() },
			fieldStack.push(layoutField))
	}
