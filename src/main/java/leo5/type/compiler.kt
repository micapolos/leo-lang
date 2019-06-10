package leo5.type

import leo.base.empty
import leo.base.toList
import leo5.compiler.*
import leo5.compiler.Field as CompiledField
import leo5.compiler.Struct as CompiledStruct
import leo5.compiler.Type as CompiledType
import leo5.compiler.type as compiledType

data class Compiler(val map: MutableMap<Type, leo5.compiler.Type>)

val newCompiler get() = Compiler(mutableMapOf())

fun Compiler.type(type: Type): leo5.compiler.Type =
	map.computeIfAbsent(type) { computeType(type) }

fun Compiler.computeType(type: Type): CompiledType = when (type) {
	is EmptyType -> compiledType(size(0), layout(empty))
	is IntType -> compiledType(size(4), layout(empty))
	is FieldType -> type(type.field.type)
	is ApplicationType -> computeType(listOf(type.application.type, type.application.field.type))
	is StructType -> computeType(type.struct.fieldStackOrNull.toList.map(Field::type))
	is ArrayType -> type(type.array.cell.type).let { cellType ->
		compiledType(size(cellType.size.int * type.array.size), layout(array(cell(cellType.size))))
	}
	is FunctionType -> TODO()
	is OneOfType -> TODO()
}

fun Compiler.computeType(typeList: List<Type>): CompiledType {
	val compiledFieldList = mutableListOf<CompiledField>()
	var size = size(0)
	for (type in typeList) {
		compiledFieldList.add(field(size.offset))
		size += type(type).size
	}
	return compiledType(size, layout(struct(compiledFieldList)))
}
