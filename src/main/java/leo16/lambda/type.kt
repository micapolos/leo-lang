package leo16.lambda

import leo.base.fold
import leo14.untyped.leoString
import leo16.names.*

data class Type(val body: TypeBody, val isStatic: Boolean) {
	override fun toString() = reflect.leoString
}

sealed class TypeBody {
	override fun toString() = reflect.leoString
}

object EmptyTypeBody : TypeBody()

data class LinkTypeBody(val link: TypeLink) : TypeBody() {
	override fun toString() = super.toString()
}

data class AlternativeTypeBody(val alternative: TypeAlternative) : TypeBody() {
	override fun toString() = super.toString()
}

data class TypeLink(val previousType: Type, val lastField: TypeField)

data class TypeAlternative(val firstType: Type, val secondType: Type)

sealed class TypeField {
	override fun toString() = reflect.leoString
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
	override fun toString() = reflect.leoString
}

data class TypeFunction(val input: Type, val output: Type) {
	override fun toString() = reflect.leoString
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
fun Type.plus(field: TypeField) = linkTo(field).body.type
fun Type.alternative(type: Type) = TypeAlternative(this, type)
val TypeLink.body: TypeBody get() = LinkTypeBody(this)
val TypeAlternative.body: TypeBody get() = AlternativeTypeBody(this)
infix fun Type.or(type: Type) = alternative(type).body.type
fun String.fieldTo(type: Type) = sentenceTo(type).field
fun type(vararg fields: TypeField) = emptyType.fold(fields) { plus(it) }
operator fun String.invoke(vararg fields: TypeField) = fieldTo(type(*fields))
operator fun String.invoke(type: Type) = fieldTo(type)
infix fun Type.giving(type: Type) = TypeFunction(this, type)
val TypeFunction.field: TypeField get() = FunctionTypeField(this)

val TypeField.selectWord
	get() =
		when (this) {
			is SentenceTypeField -> sentence.word
			is FunctionTypeField -> _taking
			is NativeTypeField -> _native
		}

val stringTypeField: TypeField = String::class.java.nativeTypeField
val intTypeField: TypeField = Int::class.java.nativeTypeField
val booleanTypeField: TypeField = _boolean(type(_false(type())).or(type(_true(type()))))