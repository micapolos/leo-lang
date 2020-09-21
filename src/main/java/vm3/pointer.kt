package vm3

import leo.base.notNullIf
import vm3.layout.Layout

data class Pointer(val byteArray: ByteArray, val index: Int, val layout: Layout)

val Pointer.boolean: Boolean
	get() =
		when (layout.body) {
			Layout.Body.Bool -> byteArray.int(index).boolean
			else -> null
		} ?: error("$this.boolean")

val Pointer.int: Int
	get() =
		when (layout.body) {
			Layout.Body.I32 -> byteArray.int(index)
			else -> null
		} ?: error("$this.int")

val Pointer.float: Float
	get() =
		when (layout.body) {
			Layout.Body.I32 -> byteArray.int(index).float
			else -> null
		} ?: error("$this.float")

operator fun Pointer.get(index: Int): Pointer =
	when (layout.body) {
		is Layout.Body.Array ->
			notNullIf(index >= 0 && index < layout.body.itemCount) {
				Pointer(
					byteArray,
					index + index * layout.body.itemLayout.size,
					layout.body.itemLayout)
			}
		else -> null
	} ?: error("$this.get($index)")

operator fun Pointer.get(name: String): Pointer =
	when (layout.body) {
		is Layout.Body.Struct ->
			layout.body.fieldIndices[name]?.let { fieldIndex ->
				layout.body.fields[fieldIndex].let { field ->
					Pointer(byteArray, index + field.offset, field.layout)
				}
			}
		else -> null
	} ?: error("$this.get($name)")

