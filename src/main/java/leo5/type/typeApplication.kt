package leo5.type

import leo5.Value
import leo5.application
import leo5.script

data class TypeApplication(val type: Type, val field: Field)

fun application(type: Type, field: Field) = TypeApplication(type, field)
fun TypeApplication.compile(value: Value) =
	value.script.application.let { application ->
		Pair(type.compile(application.value), field.compile(application.line))
	}