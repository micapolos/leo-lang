package vm3.value.type

import leo.base.failIfOr
import leo.stak.Stak
import leo.stak.push
import leo.stak.stakOf
import leo.stak.top
import vm3.dsl.type.get
import vm3.type.Type
import vm3.value.Value

data class Scope(
	val paramStak: Stak<Type>,
	val fn: (Value) -> Type
)

val Value.type: Type get() = Scope(stakOf(), Value::type).type(this)

fun Scope.type(value: Value): Type =
	when (value) {
		is Value.Argument -> paramStak.top(value.depth)!!
		is Value.F32 -> Type.F32
		is Value.Struct -> Type.Struct(value.fields.map { typeField(it) })
		is Value.StructAt -> fn(value.lhs)[value.name]
		is Value.Switch -> TODO()
		is Value.Call -> failIfOr(fn(value.param) != value.function.param) {
			push(value.function.param).type(value.function.body)
		}
		is Value.Plus -> opType(value.lhs, value.rhs)
		is Value.Minus -> opType(value.lhs, value.rhs)
		is Value.Times -> opType(value.lhs, value.rhs)
	}

fun Scope.typeField(field: Value.Field): Type.Field =
	Type.Field(field.name, fn(field.value))

fun Scope.opType(lhs: Value, rhs: Value) =
	fn(lhs).run {
		fn(rhs).let { rhsType ->
			if (this == Type.F32 && rhsType == Type.F32) Type.F32
			else null!!
		}
	}

fun Scope.push(param: Type) = copy(paramStak = paramStak.push(param))