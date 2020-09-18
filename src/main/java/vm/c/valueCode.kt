package vm.c

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.bind
import leo.base.effect
import leo.base.persistentListMap
import leo.base.update
import vm.ArrayAccessOp
import vm.ArrayOp
import vm.ChoiceOp
import vm.FieldAccessOp
import vm.LocalOp
import vm.NativeOp
import vm.Op
import vm.StructOp
import vm.SwitchOp
import vm.Value

data class ValueCodegen(
	val valueNames: PersistentMap<Value, String> = persistentMapOf(),
	val bodyCode: String = ""
)

fun ValueCodegen.name(value: Value): Effect<ValueCodegen, String> =
	valueNames[value].let { nameOrNull ->
		if (nameOrNull != null) effect(nameOrNull)
		else codeEffect(value.op).bind { valueCode ->
			"v${valueNames.size}".let { varName ->
				ValueCodegen(
					valueNames.put(value, varName),
					"$bodyCode${value.type.code} $varName = $valueCode; ") effect varName
			}
		}
	}

fun ValueCodegen.codeEffect(op: Op): Effect<ValueCodegen, String> =
	when (op) {
		is NativeOp -> effect("${op.native}")
		is LocalOp -> effect("local${op.index}")
		is ArrayOp -> effect(op.values)
			.persistentListMap { name(it) }
			.update { "{ ${it.joinToString(", ")} }" }
		is FieldAccessOp ->
			name(op.lhs).bind { lhs ->
				effect("${lhs}.${op.name}")
			}
		is ArrayAccessOp ->
			name(op.lhs).bind { lhs ->
				name(op.index).bind { index ->
					effect("$lhs[$index]")
				}
			}
		is StructOp -> effect(op.fields)
			.persistentListMap { name(it.value) }
			.update { "{ ${it.joinToString(", ")} }" }
		is SwitchOp -> TODO()
		is ChoiceOp -> TODO()
	}

val Value.bodyCode
	get() =
		ValueCodegen().name(this).bind { name ->
			"${bodyCode}return ${name};"
		}
