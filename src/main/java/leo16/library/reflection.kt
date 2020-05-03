package leo16.library

import leo13.array
import leo14.untyped.typed.loadClass
import leo15.*
import leo16.*
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

private val nameClassDefinition =
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

private val classFieldDefinition =
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

private val fieldGetDefinition =
	value(getName(fieldName(nativeName(anyName())))).gives {
		val field = this
			.getOrNull(getName)!!
			.getOrNull(fieldName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Field
		field.get(null).nativeValue
	}

private val objectGetFieldDefinition =
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

private val classConstructorDefinition =
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

private val constructorInvokeDefinition =
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

private val classMethodDefinition =
	value(
		className(nativeName(anyName())),
		methodName(
			nameName(textName(nativeName(anyName()))),
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
		methodName(nativeName(anyName())),
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

private val objectInvokeMethodDefinition =
	value(
		nativeName(anyName()),
		invokeName(
			methodName(nativeName(anyName())),
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

private val reflectionDictionary =
	emptyDictionary
		.plus(nameClassDefinition)
		.plus(classFieldDefinition)
		.plus(fieldGetDefinition)
		.plus(objectGetFieldDefinition)
		.plus(classConstructorDefinition)
		.plus(constructorInvokeDefinition)
		.plus(classMethodDefinition)
		.plus(methodInvokeDefinition)
		.plus(objectInvokeMethodDefinition)

val reflection = reflectionDictionary.field.value