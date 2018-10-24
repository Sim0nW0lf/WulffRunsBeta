grammar Demo;

WHITESPACE: [ \t\n\r]+->skip;		
NUMBER: '-'? [0-9]+ (DOT NUMBER)?;
LETTER: ('a-z' | 'A-z')+;
DOT: [.];

prog: expression;

expression: '(' expression ')'
		  | expression '/' expression
		  | expression '*' expression
		  | expression '+' expression
		  | expression '-' expression
		  |	NUMBER (';' | expression)?
		  ;