package leo32.rt

import leo.base.fold
import leo.base.map
import leo32.base.List
import leo32.base.seq

data class Vector(
	val symbol: Symbol,
	val valueList: List<Value>)

fun Scope.at(vector: Vector, symbol: Symbol) = null

fun Scope.value(vector: Vector): Value =
	emptyValue.fold(vector.valueList.seq) { value ->
		plus(vector.symbol to value)
	}

fun Scope.fieldSeq(vector: Vector) =
	vector.valueList.seq.map {
		vector.symbol to this
	}
