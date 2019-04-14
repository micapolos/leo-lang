package leo32.rt

import leo.base.Empty
import leo.base.fold
import leo.base.notNullIf
import leo.base.seq
import leo32.base.*
import leo32.base.List

data class Switch(
	val entryList: List<Entry>,
	val valueToValueTree: Tree<Value?>)

val Empty.switch
	get() =
		Switch(list(), tree())

fun Switch.plus(entry: Entry) =
	Switch(
		entryList.add(entry),
		valueToValueTree.put(entry.key.bitSeq, entry.value))

fun Scope.field(switch: Switch) =
	switchSymbol to emptyValue.fold(switch.entryList.seq) { entry ->
		fold(entry.fieldSeq) { field ->
			plus(field)
		}
	}

fun Scope.at(switch: Switch, symbol: Symbol) =
	notNullIf(symbol == entrySymbol && switch.entryList.size.int == 1) {
		emptyValue.plus(switch.entryList.at(0.i32).toField)
	}

fun Scope.fieldSeq(switch: Switch) =
	seq(field(switch))

