package leo21.type

fun Type.access(name: String): Type =
	when (this) {
		is StructType -> struct.access(name)
		is ChoiceType -> choice.access(name)
		is RecursiveType -> recursive.access(name)
		is RecurseType -> recurse.access(name)
	}

fun Struct.access(name: String): Type = TODO()
fun Choice.access(name: String): Type = TODO()
fun Recursive.access(name: String): Type = TODO()
fun Recurse.access(name: String): Type = TODO()
