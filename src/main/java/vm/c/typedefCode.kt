package vm.c

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.bind
import leo.base.effect
import leo.base.persistentListMap
import leo.base.update
import vm.ArrayType
import vm.ChoiceType
import vm.Field
import vm.IndexType
import vm.NativeType
import vm.StructType
import vm.Type

data class TypedefCodegen(
	val typeNames: PersistentMap<Type, String> = persistentMapOf(),
	val defCode: String = ""
)

val Type.defCode: String
	get() =
		TypedefCodegen().add(this).defCode

fun TypedefCodegen.add(type: Type): TypedefCodegen =
	nameEffect(type).state

fun TypedefCodegen.nameEffect(type: Type): Effect<TypedefCodegen, String> =
	if (type is NativeType) effect(type.native.nativeTypeCode)
	else typeNames[type].let { nameOrNull ->
		if (nameOrNull != null) effect(nameOrNull)
		else codeEffect(type).bind { typeCode ->
			"t${typeNames.size}".let { typeName ->
				TypedefCodegen(
					typeNames.put(type, typeName),
					"${defCode}typedef $typeCode $typeName;\n") effect typeName
			}
		}
	}

fun TypedefCodegen.codeEffect(type: Type): Effect<TypedefCodegen, String> =
	when (type) {
		is NativeType -> effect(type.native.nativeTypeCode)
		is IndexType -> effect("int")
		is StructType -> codeEffect(type.fields).update { "struct { $it }" }
		is ChoiceType -> TODO()
		is ArrayType -> TODO()
	}

fun TypedefCodegen.codeEffect(fields: PersistentList<Field>): Effect<TypedefCodegen, String> =
	effect(fields)
		.persistentListMap { field ->
			nameEffect(field.type).bind { typeName ->
				effect("$typeName ${field.name};")
			}
		}
		.update { it.joinToString(" ") }
