package vm3.type.layout

import vm3.getOrCompute
import vm3.layout.Layout
import vm3.type.Type

data class Layouts(val map: MutableMap<Type, Layout> = HashMap())

operator fun Layouts.get(type: Type): Layout =
	map.getOrCompute(type) { newLayout(type) }

fun Layouts.newLayout(type: Type): Layout =
	when (type) {
		Type.F32 -> Layout(4, Layout.Body.F32)
		is Type.Struct -> type.fields
			.map { get(it.valueType) }
			.let { fieldLayouts ->
				var offset = 0
				val layoutFields = fieldLayouts.map { fieldLayout ->
					Layout.Body.Field(offset, fieldLayout).also { offset += fieldLayout.size }
				}
				Layout(
					layoutFields.sumBy { it.layout.size },
					Layout.Body.Struct(
						hashMapOf(*type.fields.map { it.name }.zip(type.fields.indices).toTypedArray()),
						layoutFields))
			}
		is Type.Choice ->
			type.caseTypes
				.map { get(it) }
				.map { fieldLayout -> Layout.Body.Field(4, fieldLayout) }
				.let { layoutFields ->
					Layout(
						4 + (layoutFields.map { it.layout.size }.max() ?: 0),
						Layout.Body.Choice(
							hashMapOf(*type.caseTypes.zip(type.caseTypes.indices).toTypedArray()),
							layoutFields))
				}
	}
