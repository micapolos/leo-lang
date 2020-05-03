package leo16

import leo13.array
import leo14.untyped.typed.loadClass
import leo15.*
import java.lang.reflect.Constructor
import java.lang.reflect.Field

fun Value.gives(apply: Value.() -> Value) =
	pattern.definitionTo(
		body {
			//nullIfThrowsException {
			apply(this)
			//} ?: this
		})

val nameClassDefinition =
	value(className(nameName(textName(nativeName(anyName()))))).gives {
		val name = this
			.getOrNull(className)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as String
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
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(fieldName)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as String
		fieldName(class_.getField(name).nativeField).value
	}

val fieldGetDefinition =
	value(getName(fieldName(nativeName(anyName())))).gives {
		val field = this
			.getOrNull(getName)!!
			.getOrNull(fieldName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Field
		field.get(null).nativeValue
	}

val objectGetFieldDefinition =
	value(
		nativeName(anyName()),
		getName(fieldName(nativeName(anyName())))
	).gives {
		val object_ = this
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value
		val field = this
			.getOrNull(getName)!!
			.getOrNull(fieldName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Field
		field.get(object_).nativeValue
	}

val classConstuctorDefinition =
	value(
		className(nativeName(anyName())),
		constructorName(parameterName(listName(anyName())))
	).gives {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Class<*>
		val classes = this
			.getOrNull(constructorName)!!
			.getOrNull(parameterName)!!
			.getOrNull(listName)!!
			.listOrNull {
				this
					.accessOrNull(className)!!
					.getOrNull(nativeName)!!
					.theNativeOrNull!!
					.value as Class<*>
			}!!
			.array
		constructorName(class_.getConstructor(*classes).nativeField).value
	}

val constructorInvokeDefinition =
	value(
		constructorName(nativeName(anyName())),
		invokeName(parameterName(listName(anyName())))
	).gives {
		val constructor = this
			.getOrNull(constructorName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Constructor<*>
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(listName)!!
			.listOrNull { theNativeOrNull!!.value }!!
			.array
		constructor.newInstance(*args).nativeValue
	}
