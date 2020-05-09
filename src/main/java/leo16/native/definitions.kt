package leo16.native

import leo13.array
import leo13.map
import leo13.stack
import leo14.untyped.typed.loadClass
import leo15.anyName
import leo15.arrayName
import leo15.booleanName
import leo15.className
import leo15.constructorName
import leo15.fieldName
import leo15.getName
import leo15.invokeName
import leo15.methodName
import leo15.nameName
import leo15.nativeName
import leo15.nullName
import leo15.objectName
import leo15.parameterName
import leo15.textName
import leo16.accessOrNull
import leo16.field
import leo16.getOrNull
import leo16.gives
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.nativeValue
import leo16.stackOrNull
import leo16.theNativeOrNull
import leo16.value
import leo16.listValue
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

fun String.definition(class_: Class<*>) =
	value(className(this())).gives {
		className(class_.nativeField).value
	}

val nativeObjectClassDefinition =
	value(className(objectName(nativeName(anyName())))).gives {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(objectName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value!!
			.javaClass
		className(class_.nativeField).value
	}

val nullNativeDefinition =
	value(nativeName(nullName())).gives {
		null.nativeValue
	}

val nativeBooleanDefinition =
	value(booleanName(nativeName(anyName()))).gives {
		this
			.getOrNull(booleanName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.run { value as Boolean }
			.field.value
	}

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
		className(anyName()),
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

val nativeGetFieldDefinition =
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

val classConstructorDefinition =
	value(
		className(nativeName(anyName())),
		constructorName(parameterName(_list(anyName())))
	).gives {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Class<*>
		val classes = this
			.getOrNull(constructorName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(className)!!
					.getOrNull(nativeName)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		constructorName(class_.getConstructor(*classes).nativeField).value
	}

val constructorInvokeDefinition =
	value(
		constructorName(nativeName(anyName())),
		invokeName(parameterName(_list(anyName())))
	).gives {
		val constructor = this
			.getOrNull(constructorName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Constructor<*>
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		constructor.newInstance(*args).nativeValue
	}

val classMethodDefinition =
	value(
		className(nativeName(anyName())),
		methodName(
			nameName(textName(nativeName(anyName()))),
			parameterName(_list(anyName()))
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
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(className)!!
					.getOrNull(nativeName)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		methodName(class_.getMethod(name, *classes).nativeField).value
	}

val methodInvokeDefinition =
	value(
		methodName(nativeName(anyName())),
		invokeName(parameterName(_list(anyName())))
	).gives {
		val method = this
			.getOrNull(methodName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(null, *args).nativeValue
	}

val nativeInvokeMethodDefinition =
	value(
		nativeName(anyName()),
		invokeName(
			methodName(nativeName(anyName())),
			parameterName(_list(anyName())))
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
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(object_, *args).nativeValue
	}

val arrayStackDefinition =
	value(_list(arrayName(nativeName(anyName())))).gives {
		val array = this
			.getOrNull(_list)!!
			.getOrNull(arrayName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Array<*>
		stack(*array).map { value(nativeField) }.listValue
	}
