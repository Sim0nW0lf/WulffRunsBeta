grammar Demo;

WHITESPACE: [ \t\n\r]+->skip;		
NUMBER: '-'? [0-9]+ (DOT NUMBER)?;
LETTER: ('a-z' | 'A-z')+;
DOT: [.];
VARIABLE: LETTER+ NUMBER*;

prog: expression;

root: expression (';' expression)* ';'?;

assignment : VARIABLE '=' expression;

expression: '(' expression ')'
		  | expression '/' expression
		  | expression '*' expression
		  | expression '+' expression
		  | expression '-' expression
		  |	NUMBER
		  | ';'
		  ;