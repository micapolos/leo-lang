any i32
called lhs
plus
	any i32
	called rhs
does
	load lhs
	load rhs
	i32 add

module
	function
		name add
		param
			name lhs
			type i32
		param
			name rhs
			type i32
		result type i32
		body
			load lhs
			load rhs
			i32 add
	export
		name "add"
		function add
