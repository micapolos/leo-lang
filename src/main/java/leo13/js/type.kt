package leo13.js

sealed class Type

object EmptyType : Type()
object NativeType : Type()
data class LinkType(val link: TypeLink) : Type()
data class FunctionType(val function: Function) : Type()

val emptyType = EmptyType
val nativeType = NativeType
fun Type.plus(line: TypeLine): Type = LinkType(this linkTo line)
