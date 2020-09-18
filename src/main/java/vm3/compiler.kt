package vm3

import java.io.ByteArrayOutputStream

data class Compiler(
	val dataOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val codeOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val valueOffsets: MutableMap<Value, Int> = HashMap(),
	val valueTypes: MutableMap<Value, Type> = HashMap(),
	val typeLayouts: MutableMap<Type, Layout> = HashMap()
)

val Fn.compiled: Compiled get() = compile(this)

fun compile(fn: Fn): Compiled {
	val compiler = Compiler()
	compiler.valueTypes[Value.Input] = fn.input
	compiler.dataOutputStream.writeHole(fn.input.size)
	val outputIndex = compiler.offset(fn.output)
	compiler.codeOutputStream.writeOp(0x00)
	return Compiled(
		compiler.codeOutputStream.toByteArray(),
		compiler.dataOutputStream.size(),
		compiler.type(fn.output),
		outputIndex)
}

fun Compiler.offset(value: Value): Int =
	valueOffsets.get(value) { compileOffset(value) }

fun Compiler.compileOffset(value: Value): Int =
	when (value) {
		is Value.Input -> 0

		is Value.Bool -> addConst(value.boolean.int)
		is Value.I32 -> addConst(value.int)
		is Value.F32 -> addConst(value.float.int)

		is Value.Array -> TODO()
		is Value.Struct -> TODO()

		is Value.ArrayAt -> TODO()
		is Value.StructAt -> TODO()

		is Value.Inc ->
			when (type(value)) {
				Type.I32 -> addOp(0x10, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Dec ->
			when (type(value)) {
				Type.I32 -> addOp(0x11, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Plus ->
			when (type(value)) {
				Type.I32 -> addOp(0x16, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(0x33, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
		is Value.Minus ->
			when (type(value)) {
				Type.I32 -> addOp(0x17, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(0x34, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
	}


fun Compiler.addConst(lhs: Int): Int =
	dataOutputStream.writeHole(4).also { dst ->
		codeOutputStream.writeByte(0x08)
		codeOutputStream.writeInt(dst)
		codeOutputStream.writeInt(lhs)
	}

fun Compiler.addOp(op: Byte, type: Type, lhs: Value): Int =
	offset(lhs).let { lhs ->
		dataOutputStream.writeHole(type.size).also { dst ->
			codeOutputStream.writeByte(op)
			codeOutputStream.writeInt(dst)
			codeOutputStream.writeInt(lhs)
		}
	}

fun Compiler.addOp(op: Byte, type: Type, lhs: Value, rhs: Value): Int =
	offset(lhs).let { lhs ->
		offset(rhs).let { rhs ->
			dataOutputStream.writeHole(type.size).also { dst ->
				codeOutputStream.writeByte(op)
				codeOutputStream.writeInt(dst)
				codeOutputStream.writeInt(lhs)
				codeOutputStream.writeInt(rhs)
			}
		}
	}

fun Compiler.type(value: Value): Type =
	valueTypes.get(value) { compileType(value) }

fun Compiler.compileType(value: Value): Type =
	when (value) {
		Value.Input -> error("unknown type")
		is Value.Bool -> Type.Bool
		is Value.I32 -> Type.I32
		is Value.F32 -> Type.F32
		is Value.Struct -> Type.Struct(value.fields.map { Type.Struct.Field(it.name, type(it.value)) })
		is Value.Array -> Type.Array(type(value.values[0]), value.values.size)
		is Value.ArrayAt -> TODO()
		is Value.StructAt -> TODO()
		is Value.Inc ->
			when (type(value.lhs)) {
				Type.I32 -> Type.I32
				else -> null
			}
		is Value.Dec ->
			when (type(value.lhs)) {
				Type.I32 -> Type.I32
				else -> null
			}
		is Value.Plus ->
			when (type(value.lhs)) {
				Type.I32 ->
					when (type(value.lhs)) {
						Type.I32 -> Type.I32
						else -> null
					}
				Type.F32 ->
					when (type(value.lhs)) {
						Type.F32 -> Type.F32
						else -> null
					}
				else -> null
			}
		is Value.Minus ->
			when (type(value.lhs)) {
				Type.I32 ->
					when (type(value.lhs)) {
						Type.I32 -> Type.I32
						else -> null
					}
				Type.F32 ->
					when (type(value.lhs)) {
						Type.F32 -> Type.F32
						else -> null
					}
				else -> null
			}
	} ?: error("type($value)")

fun Compiler.layout(type: Type): Layout =
	typeLayouts.get(type) { compileLayout(type) }

fun Compiler.compileLayout(type: Type): Layout =
	when (type) {
		Type.Bool -> Layout(4, Layout.Body.Bool)
		Type.I32 -> Layout(4, Layout.Body.I32)
		Type.F32 -> Layout(4, Layout.Body.F32)
		is Type.Array -> layout(type.itemType).let { itemLayout ->
			Layout(type.itemCount * itemLayout.size, Layout.Body.Array(itemLayout, type.itemCount))
		}
		is Type.Struct -> type.fields
			.map { layout(it.valueType) }
			.let { fieldLayouts ->
				var offset = 0
				val layoutFields = fieldLayouts.map { fieldLayout ->
					Layout.Body.Struct.Field(offset, fieldLayout).also { offset += fieldLayout.size }
				}
				Layout(
					layoutFields.sumBy { it.layout.size },
					Layout.Body.Struct(
						hashMapOf(*type.fields.map { it.name }.zip(type.fields.indices).toTypedArray()),
						layoutFields))
			}
	}
