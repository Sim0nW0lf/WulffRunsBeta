grammar Demo;


WHITESPACE: [ \t\n\r]+->skip;	

E: 'e';
VARIABLE: LETTER+ NUMBER* VARIABLE*;	
NUMBER: ([0-9]+ (DOT [0-9]+)?) | (DOT [0-9]+);
LETTER: ([a-z] | [A-Z])+;
DOT: [.];
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
POW: '^' | '**';
L_BRACKET: '(';
R_BRACKET: ')';
L_CBRACKET: '{';
R_CBRACKET: '}';
ASSIGN: '=';
MOD: 'mod' | '%';
TERMINATOR: ';';
SEPERATOR: ',';
MATRIX_PREFIX: 'm:';
FROM: 'from';
TO: 'to';
FROMTOFUNCTIONS: DIFFERENTIATE | INTEGRATE;
DIFFERENTIATE: 'div';
INTEGRATE: 'int';

root: statement (TERMINATOR statement)* TERMINATOR ? EOF;

statement: (assignment | expression | functionDefinition | matrixExpression | matrixDefinition);

matrixDefinition: name=VARIABLE ASSIGN L_CBRACKET (matrixRow TERMINATOR)* R_CBRACKET;
matrixRow: expression (SEPERATOR expression)*;
matrixCall: MATRIX_PREFIX name = VARIABLE;

assignment : VARIABLE ASSIGN expression;

functionDefinition: name=VARIABLE L_BRACKET (VARIABLE (SEPERATOR VARIABLE)*) R_BRACKET ASSIGN expression;	
functionCall: name=VARIABLE L_BRACKET (expression (SEPERATOR expression)*) R_BRACKET;			

expression: sign = SUB? L_BRACKET expression R_BRACKET						#Bracket
		  | links = expression E rechts = expression						#Tiny
		  |<assoc=right> links =  expression POW rechts = expression 		#Power
		  | links = expression DIV rechts = expression						#Division
		  | links = expression MUL rechts = expression						#Multiplikation
		  |<assoc=right> links = expression MOD rechts = expression			#Modulo
		  | links = expression SUB rechts = expression						#Subtraktion
		  | links = expression ADD rechts = expression						#Addition
		  | functionCall													#FunctionCaller
		  | sign = (SUB | ADD)? NUMBER										#Number
		  | sign = (SUB | ADD)? VARIABLE									#Variable
		  ;

matrixExpression: links = matrixCall MUL rechts = matrixCall				#MatrixMultiplikation
				;
				
fromToFunction: type = FROMTOFUNCTIONS FROM a = expression TO b = expression f = functionCall;




