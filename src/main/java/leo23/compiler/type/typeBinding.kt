package leo23.compiler.type

import leo23.type.Type
import java.sql.Types

data class TypeBinding(val keyTypes: Types, val valueType: Type)