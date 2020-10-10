package leo19.type

import leo14.Script
import leo14.fieldTo
import leo14.lineTo
import leo14.literal
import leo14.script
import leo19.value.IndexedValue
import leo19.value.IntValue
import leo19.value.ListValue
import leo19.value.NativeValue
import leo19.value.Value

fun Type.script(value: Value): Script =
	when (this) {
		is StructType -> struct.script(value)
		is ChoiceType -> choice.script(value)
		is ArrowType -> script("<function>" lineTo script())
		is NativeType -> when (native) {
			Int::class -> script(literal((value as NativeValue).any as Int))
			String::class -> script(literal((value as NativeValue).any as String))
			else -> TODO()
		}
	}

fun Struct.script(value: Value): Script =
	when (fieldList.size) {
		0 -> script()
		1 -> fieldList[0].let { script(it.name lineTo it.type.script(value)) }
		else -> fieldList
			.zip((value as ListValue).list)
			.map { it.first.name lineTo it.first.type.script(it.second) }
			.let { script(*it.toTypedArray()) }
	}

fun Choice.script(value: Value): Script =
	if (fieldList.all { it.type == struct() })
		(value as IntValue).int.let { index ->
			fieldList[index].run {
				script(name fieldTo script())
			}
		}
	else
		(value as IndexedValue).let { indexed ->
			fieldList[indexed.index].let {
				script(it.name lineTo it.type.script(indexed.value))
			}
		}
