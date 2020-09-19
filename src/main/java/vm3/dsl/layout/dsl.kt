package vm3.dsl.layout

import vm3.Layout
import vm3.checkIndex

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
					Layout.Body.Field(offset, fieldLayout).also { offset += fieldLayout.size }
				}
			}))

val Layout.itemSize: Int
	get() =
		(body as Layout.Body.Array).itemLayout.size

fun Layout.offset(index: Int): Int =
	(body as Layout.Body.Array).offset(index)

fun Layout.offset(name: String): Int =
	(body as Layout.Body.Struct).offset(name)

fun Layout.Body.Array.offset(index: Int): Int =
	checkIndex(itemCount, index).run {
		itemLayout.size * index
	}

fun Layout.Body.Struct.offset(name: String): Int =
	fields[fieldIndices[name]!!].offset
