package leo16

import leo14.untyped.typed.loadClass
import leo15.*
import java.lang.reflect.Field

fun Value.gives(apply: Value.() -> Value) =
	pattern.definitionTo(
		body {
			nullIfThrowsException {
				apply(this)
			} ?: this
		})

val nameClassDefinition =
	value(className(nameName(textName(nativeName(anyName()))))).gives {
		val name = this
			.getOrNull(className)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.nativeOrNull!! as String
		className(name.loadClass.nativeField).value
	}

val classFieldDefinition =
	value(
		className(nativeName(anyName())),
		fieldName(nameName(textName(nativeName(anyName()))))
	).gives {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.nativeOrNull!! as Class<*>
		val name = this
			.getOrNull(fieldName)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.nativeOrNull!! as String
		fieldName(class_.getField(name).nativeField).value
	}

val fieldGetDefinition =
	value(getName(fieldName(nativeName(anyName())))).gives {
		val field = this
			.getOrNull(getName)!!
			.getOrNull(fieldName)!!
			.getOrNull(nativeName)!!
			.nativeOrNull!! as Field
		field.get(null).nativeValue
	}
