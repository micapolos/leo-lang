package leo19.type

import leo13.EmptyStack
import leo13.LinkStack
import leo13.all
import leo13.toList
import leo14.Script
import leo14.fieldTo
import leo14.lineTo
import leo14.script
import leo19.value.ArrayValue
import leo19.value.IntValue
import leo19.value.Value

// TODO: This file seems to be not used.

fun Type.script(value: Value): Script =
	when (this) {
		is StructType -> struct.script(value)
		is ChoiceType -> choice.script(value)
		is ArrowType -> script("<function>" lineTo script())
	}

fun Struct.script(value: Value): Script =
	when (fieldStack) {
		is EmptyStack -> script()
		is LinkStack -> when (fieldStack.link.stack) {
			is EmptyStack -> fieldStack.link.value.let { script(it.name lineTo it.type.script(value)) }
			is LinkStack -> fieldStack.toList()
				.zip((value as ArrayValue).list)
				.map { it.first.name lineTo it.first.type.script(it.second) }
				.let { script(*it.toTypedArray()) }
		}
	}

fun Choice.script(value: Value): Script =
	if (caseStack.all { this.type == type() })
		(value as IntValue).int.let { index ->
			caseStack.toList()[index].run {
				script(name fieldTo script())
			}
		}
	else
		(value as ArrayValue).let { array ->
			caseStack.toList()[(array.list[0] as IntValue).int].let {
				script(it.name lineTo it.type.script(array.list[1]))
			}
		}
