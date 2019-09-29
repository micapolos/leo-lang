define
	letter
	contains: options: a b c

	stack
	contains: options
		empty
		link
			tail: stack
			head: letter

function: recursive
	stack rebuild
	to: stack
	gives: given stack match
		empty: given stack
		link: stack: link
			tail: matching tail stack rebuild
			head: matching head letter
