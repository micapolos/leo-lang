package leo32.rt

import leo.base.fold
import leo.base.notNullIf
import leo.base.seq
import leo32.base.I32
import leo32.base.bitSeq

fun Scope.bitsValue(i32: I32) =
	emptyValue.fold(i32.bitSeq) { bit ->
		plus(field(bit))
	}

fun Scope.at(i32: I32, symbol: Symbol) =
	notNullIf(symbol == i32Symbol) { bitsValue(i32) }

fun Scope.field(i32: I32) =
	i32Symbol to bitsValue(i32)

fun Scope.fieldSeq(i32: I32) =
	seq(field(i32))
