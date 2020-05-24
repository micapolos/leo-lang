package leo16.lambda

data class Type(val body: TypeBody, val isStatic: Boolean) {
	override fun toString() = asField.toString()
}

sealed class TypeBody {
	override fun toString() = asField.toString()
}

object EmptyTypeBody : TypeBody()

data class LinkTypeBody(val link: TypeLink) : TypeBody() {
	override fun toString() = super.toString()
}

data class AlternativeTypeBody(val alternative: TypeAlternative) : TypeBody() {
	override fun toString() = super.toString()
}

data class TypeLink(val type: Type, val field: TypeField)

data class TypeAlternative(val firstType: Type, val secondType: Type)

sealed class TypeField {
	override fun toString() = asField.toString()
}

data class SentenceTypeField(val sentence: TypeSentence) : TypeField() {
	override fun toString() = super.toString()
}

data class FunctionTypeField(val function: TypeFunction) : TypeField() {
	override fun toString() = super.toString()
}

data class NativeTypeField(val native: Any) : TypeField() {
	override fun toString() = super.toString()
}

data class TypeSentence(val word: String, val type: Type) {
	override fun toString() = asField.toString()
}

data class TypeFunction(val input: Type, val output: Type) {
	override fun toString() = asField.toString()
}

val emptyTypeBody: TypeBody = EmptyTypeBody
val emptyType = emptyTypeBody.type
val TypeBody.type: Type get() = Type(this, isStatic)
val TypeSentence.field: TypeField get() = SentenceTypeField(this)
val Any.nativeTypeField: TypeField get() = NativeTypeField(this)
val emptyTypeCase: TypeBody = EmptyTypeBody
val TypeLink.case: TypeBody get() = LinkTypeBody(this)
fun Type.linkTo(field: TypeField) = TypeLink(this, field)
fun String.sentenceTo(type: Type) = TypeSentence(this, type)
val TypeField.sentenceOrNull get() = (this as? SentenceTypeField)?.sentence
val TypeField.functionOrNull get() = (this as? FunctionTypeField)?.function
val TypeField.nativeOrNull get() = (this as? NativeTypeField)?.native
val Type.isEmpty get() = body.isEmpty
val TypeBody.isEmpty get() = (this is EmptyTypeBody)
val TypeBody.linkOrNull get() = (this as? LinkTypeBody)?.link
val TypeBody.alternativeOrNull get() = (this as? AlternativeTypeBody)?.alternative
fun Type.plus(field: TypeField) = linkTo(field).type
fun Type.alternative(type: Type) = TypeAlternative(this, type)
val TypeAlternative.body: TypeBody get() = AlternativeTypeBody(this)
infix fun Type.or(type: Type) = alternative(type).body.type