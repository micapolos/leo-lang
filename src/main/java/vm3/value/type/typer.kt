package vm3.value.type

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.bind
import leo.base.effect
import leo.base.fold
import leo.base.seq
import leo.base.stack
import leo.base.update
import leo.base.updateState
import leo.stak.Stak
import leo.stak.push
import leo.stak.stakOf
import leo.stak.top
import leo13.push
import leo13.stack
import leo13.toList
import vm3.dsl.type.get
import vm3.type.Type
import vm3.value.Value

data class Typer(
	val argumentTypeStak: Stak<Type>,
	val valueTypeMap: PersistentMap<Value, Type>
)

val emptyTyper = Typer(stakOf(), persistentMapOf())

fun Typer.typeEffect(value: Value): Effect<Typer, Type> =
	valueTypeMap[value]
		?.let { effect(it) }
		?: computeTypeEffect(value).run {
			state.put(value to this.value) effect this.value
		}

fun Typer.computeTypeEffect(value: Value): Effect<Typer, Type> =
	when (value) {
		is Value.Argument -> effect(argumentTypeStak.top(value.depth)!!)
		is Value.F32 -> effect(Type.F32)
		is Value.Struct ->
			effect(stack<Type.Field>()).fold(value.fields.seq) { field ->
				typeEffect(field.value).update { type ->
					this@fold.value.push(Type.Field(field.name, type))
				}
			}.update { fieldStack ->
				Type.Struct(fieldStack.toList())
			}
		is Value.StructAt -> typeEffect(value.lhs).bind { lhs ->
			lhs as Type.Struct
			effect(lhs[value.name])
		}
		is Value.Switch -> TODO()
		is Value.Call -> push(value.function.param)
			.typeEffect(value.function.body)
			.updateState { this@computeTypeEffect }
		is Value.Plus ->
			typeEffect(value.lhs).bind { lhs ->
				typeEffect(value.rhs).bind { rhs ->
					if (lhs == Type.F32 && rhs == Type.F32) effect(Type.F32)
					else error("${value.lhs}.plus(${value.rhs}).type")
				}
			}
		is Value.Minus ->
			typeEffect(value.lhs).bind { lhs ->
				typeEffect(value.rhs).bind { rhs ->
					if (lhs == Type.F32 && rhs == Type.F32) effect(Type.F32)
					else error("${value.lhs}.minus(${value.rhs}).type")
				}
			}
		is Value.Times ->
			typeEffect(value.lhs).bind { lhs ->
				typeEffect(value.rhs).bind { rhs ->
					if (lhs == Type.F32 && rhs == Type.F32) effect(Type.F32)
					else error("${value.lhs}.times(${value.rhs}).type")
				}
			}
	}

fun Typer.put(entry: Pair<Value, Type>) =
	copy(valueTypeMap = valueTypeMap.put(entry.first, entry.second))

fun Typer.push(param: Type) =
	copy(argumentTypeStak = argumentTypeStak.push(param))
