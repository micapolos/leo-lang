package leo16.library

import leo13.array
import leo13.stack
import leo14.untyped.typed.loadClass
import leo15.*
import leo16.*
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

private fun String.definition(class_: Class<*>) =
	value(className(this())).gives {
		className(class_.nativeField).value
	}

private val nameClassDefinition =
	value(className(nameName(textName(anyName())))).gives {
		val name = this
			.getOrNull(className)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as String
		className(name.loadClass.nativeField).value
	}

private val classFieldDefinition =
	value(
		className(anyName()),
		fieldName(nameName(textName(anyName())))
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

private val fieldGetDefinition =
	value(getName(fieldName(anyName()))).gives {
		val field = this
			.getOrNull(getName)!!
			.getOrNull(fieldName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Field
		field.get(null).nativeValue
	}

private val nativeGetFieldDefinition =
	value(
		nativeName(anyName()),
		getName(fieldName(anyName()))
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

private val classConstructorDefinition =
	value(
		className(anyName()),
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

private val constructorInvokeDefinition =
	value(
		constructorName(anyName()),
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

private val classMethodDefinition =
	value(
		className(anyName()),
		methodName(
			nameName(textName(anyName())),
			parameterName(listName(anyName()))
		)
	).gives {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(methodName)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as String
		val classes = this
			.getOrNull(methodName)!!
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
		methodName(class_.getMethod(name, *classes).nativeField).value
	}

private val methodInvokeDefinition =
	value(
		methodName(anyName()),
		invokeName(parameterName(listName(anyName())))
	).gives {
		val method = this
			.getOrNull(methodName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(listName)!!
			.listOrNull { theNativeOrNull!!.value }!!
			.array
		method.invoke(null, *args).nativeValue
	}

private val nativeInvokeMethodDefinition =
	value(
		nativeName(anyName()),
		invokeName(
			methodName(anyName()),
			parameterName(listName(anyName())))
	).gives {
		val object_ = this
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value
		val method = this
			.getOrNull(invokeName)!!
			.getOrNull(methodName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(listName)!!
			.listOrNull { theNativeOrNull!!.value }!!
			.array
		method.invoke(object_, *args).nativeValue
	}

private val arrayListDefinition =
	value(listName(arrayName(anyName()))).gives {
		val array = this
			.getOrNull(listName)!!
			.thingOrNull!! // Not ideal, but probably OK.
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Array<*>
		stack(*array).expandField { nativeField }.value
	}

private val reflectionDictionary =
	emptyDictionary
		.plus(booleanName.definition(Boolean::class.java))
		.plus(charName.definition(Char::class.java))
		.plus(byteName.definition(Byte::class.java))
		.plus(shortName.definition(Short::class.java))
		.plus(intName.definition(Int::class.java))
		.plus(longName.definition(Long::class.java))
		.plus(floatName.definition(Float::class.java))
		.plus(doubleName.definition(Double::class.java))
		.plus(nameClassDefinition)
		.plus(classFieldDefinition)
		.plus(fieldGetDefinition)
		.plus(nativeGetFieldDefinition)
		.plus(classConstructorDefinition)
		.plus(constructorInvokeDefinition)
		.plus(classMethodDefinition)
		.plus(methodInvokeDefinition)
		.plus(nativeInvokeMethodDefinition)
		.plus(arrayListDefinition)

val nativeReflection = reflectionDictionary.field.value