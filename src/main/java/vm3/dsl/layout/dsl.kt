package vm3.dsl.layout

import vm3.Layout

val bool: Layout = Layout(4, Layout.Body.Bool)
val i32: Layout = Layout(4, Layout.Body.I32)
val f32: Layout = Layout(4, Layout.Body.F32)
operator fun Layout.get(itemCount: Int): Layout =
	Layout(
		size * itemCount,
		Layout.Body.Array(this, itemCount))

fun struct(vararg fields: Pair<String, Layout>): Layout =
	Layout(
		fields.sumBy { it.second.size },
		Layout.Body.Struct(
			hashMapOf(*fields.map { it.first }.zip(fields.indices).toTypedArray()),
			Unit.run {
				var offset = 0
				fields.map { (fieldName, fieldLayout) ->
					Layout.Body.Struct.Field(offset, fieldLayout).also { offset += fieldLayout.size }
				}
			}))
