package leo14.syntax

sealed class Kind

data class ValueKind(val isKeyword: Boolean) : Kind()
data class TypeKind(val isKeyword: Boolean) : Kind()
object CommentKind : Kind()

val valueKind: Kind = ValueKind(false)
val valueKeywordKind: Kind = ValueKind(true)
val typeKind: Kind = TypeKind(false)
val typeKeywordKind: Kind = TypeKind(true)
val commentKind: Kind = CommentKind
