grammar Demo;


WHITESPACE: [ \t\n\r]+->skip;	
VARIABLE: LETTER+ NUMBER*;	
NUMBER: ('-' | '+')? UNSIGNED_NUMBER;
UNSIGNED_NUMBER: [0-9]+ (DOT UNSIGNED_NUMBER)?;
LETTER: ('a-z' | 'A-z')+;
DOT: [.];


prog: root;

root: statement (';' statement)* ';'?;

statement: (assignment | expression | functionDefinition);

assignment : VARIABLE '=' expression;

varList: VARIABLE (',' VARIABLE);
functionDefinition: LETTER '(' varList ')' '=' expression;
functionCall: LETTER '(' varList ')' '=' expression;

expression: '(' expression ')'
		  | expression 'e' expression
		  | expression ('^' | '**') expression
		  | expression '/' expression
		  | expression '*' expression
		  | expression ('%' | 'mod') expression
		  | expression '+' expression
		  | expression '-' expression
		  | functionCall
		  | NUMBER
		  | VARIABLE
		  ;