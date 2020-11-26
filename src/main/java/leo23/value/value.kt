package leo23.value

import leo14.Number
import leo14.number
import leo23.term.eval.Fn

typealias Value = Any

val Int.value get() = number(this)
val Double.value get() = number(this)

val Value.string: String get() = this as String
val Value.number: Number get() = this as Number
val Value.int: Int get() = (this as Number).bigDecimal.intValueExact()
val Value.boolean: Boolean get() = this as Boolean
val Value.pair: Pair<Value, Value> get() = this as Pair<Value, Value>
val Value.list: List<Value> get() = this as List<Value>
val Value.fn: Fn get() = this as Fn
val Value.indexed: IndexedValue<Value> get() = this as IndexedValue<Value>