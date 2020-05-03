package leo16

import leo14.untyped.typed.loadClass
import leo15.*

fun Value.gives(apply: Value.() -> Value) =
	pattern.definitionTo(
		body {
			nullIfThrowsException {
				apply(this)
			} ?: this
		})

val nameClassDefinition =
	value(className(nameName(textName(anyName())))).gives {
		this
			.getOrNull(className)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.matchText { text ->
				className(text.loadClass.nativeField).value
			}!!
	}

val classFieldDefinition =
	value(
		className(nativeName(anyName())),
		fieldName(nameName(textName(nativeName(anyName()))))
	).gives {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.onlyFieldOrNull!!
			.theNativeOrNull!!.value as Class<*>
		val name = this
			.getOrNull(fieldName)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.onlyFieldOrNull!!
			.theNativeOrNull!!
			.value as String
		fieldName(class_.getField(name).nativeField).value
	}
