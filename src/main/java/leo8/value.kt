package leo8

sealed class Value

data class DictionaryValue(val dictionary: Dictionary) : Value()
data class IntegerValue(val integer: Integer) : Value()
data class ReferenceValue(val reference: Reference) : Value()

fun value(dictionary: Dictionary): Value = DictionaryValue(dictionary)
fun value(integer: Integer): Value = IntegerValue(integer)
fun value(reference: Reference): Value = ReferenceValue(reference)
