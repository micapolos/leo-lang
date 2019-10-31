package leo13.js

sealed class Type

object EmptyType : Type()
object NullType : Type()
object DoubleType : Type()
object StringType : Type()
object NativeType : Type()
data class LinkType(val link: TypeLink) : Type()
data class ArrowType(val arrow: Arrow) : Type()

val emptyType = EmptyType
val nullType = NullType
val doubleType = DoubleType
val stringType = StringType
val nativeType = NativeType
fun Type.plus(line: TypeLine): Type = LinkType(this linkTo line)
fun type(arrow: Arrow): Type = ArrowType(arrow)