grammar Demo;

@DemoParser::members {
  @Override
  public void reportError(RecognitionException e) {
    throw new IllegalArgumentException("I quit!\n" + e.getMessage()); 
  }
  @Override
  public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException ex)
  {
    throw new IllegalArgumentException(msg); 
  }
}

@DemoLexer::members {
  @Override
  public void reportError(RecognitionException e) {
    throw new IllegalArgumentException("I quit!\n" + e.getMessage()); 
  }
  @Override
  public void recover(RecognitionException ex) 
  {
    throw new IllegalArgumentException(ex.getMessage()); 
  }
}

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
LBRACKET: '(';
RBRACKET: ')';
ASSIGN: '=';
MOD: 'mod' | '%';
TERMINATOR: ';';
SEPERATOR: ',';

root: statement (TERMINATOR statement)* TERMINATOR ? EOF;

statement: (assignment | expression | functionDefinition);

assignment : VARIABLE ASSIGN expression;

expressionList: expression (SEPERATOR expression)*;
varList: VARIABLE (SEPERATOR VARIABLE)*;
functionDefinition: VARIABLE LBRACKET varList RBRACKET ASSIGN expression;
functionCall: VARIABLE LBRACKET expressionList RBRACKET;

expression: SUB? LBRACKET expression RBRACKET			#Bracket
		  | expression E expression						#Tiny
		  |<assoc=right> expression (POW) expression 	#Power
		  | expression MUL expression					#Multiplikation
		  | expression DIV expression					#Division
		  |<assoc=right> expression (MOD) expression	#Modulo
		  | expression SUB expression					#Subtraktion
		  | expression ADD expression					#Addition
		  | functionCall								#Function
		  | (SUB | ADD)? NUMBER							#Number
		  | (SUB | ADD)? VARIABLE						#Variable
		  ;