package leo5.type

import leo.base.Stack
import leo.base.push
import leo.base.toList
import leo5.Value
import leo5.application
import leo5.empty
import leo5.script

data class TypeArray(val cell: Cell, val size: Int)

fun array(cell: Cell, size: Int) = TypeArray(cell, size)

fun TypeArray.compile(value: Value): Any =
	compile(value, null).toList

fun TypeArray.compile(value: Value, acc: Stack<Any>?): Stack<Any>? =
	if (size == 0) value.script.empty.run { acc }
	else value.script.application.let { application ->
		array(type, size - 1).compile(application.value, acc.push(type.compile(leo5.value(application.line))))
	}