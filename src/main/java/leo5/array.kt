package leo5

data class Array(val cell: Cell, val size: Size)

fun array(cell: Cell, size: Size) = Array(cell, size)
fun Array.contains(value: Value): Boolean =
	if (size.int == 0) value.isEmpty
	else value.scriptOrNull?.applicationOrNull.let { application ->
		application != null
			&& cell.contains(application.line)
			&& array(cell, size.decrement).contains(application.value)
	}