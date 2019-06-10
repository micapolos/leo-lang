package leo5.type

import leo.base.Stack
import leo.base.push
import leo.base.stackOrNull
import leo.base.toList
import leo5.Value
import leo5.application
import leo5.empty
import leo5.script

data class TypeStruct(val fieldStackOrNull: Stack<Field>?)

fun struct(fieldStackOrNull: Stack<Field>?) = TypeStruct(fieldStackOrNull)
fun struct(vararg fields: Field) = struct(stackOrNull(*fields))

fun TypeStruct.compile(value: Value): Any =
	compile(value, null).toList

fun TypeStruct.compile(value: Value, acc: Stack<Any>?): Stack<Any>? =
	fieldStackOrNull
		?.let { fieldStack ->
			value.script.application.let { application ->
				fieldStack.head.compile(application.line).let { compiledField ->
					struct(fieldStack.tail).compile(application.value, acc.push(compiledField))
				}
			}
		} ?: value.script.empty.run { acc }
