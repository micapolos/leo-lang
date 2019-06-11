package leo5

data class StructExtension(val struct: Struct, val field: Field)

fun extension(struct: Struct, field: Field) = StructExtension(struct, field)
fun StructExtension.contains(application: Application) =
	struct.contains(application.value) && field.contains(application.line)
