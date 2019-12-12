/*
 * x^2 functions
 */
double x_quadrat(double x){
	return x*x;
}

double x_quadrat_diff(double x){
	return 2*x;
}

double x_quadrat_int(double x){
	return pow(x,3)/3;
}

/*
 * e functions
 */
double my_exp(double x){
	return exp(x);
}

/*
 * sin functions
 */
double my_sin(double x){
	return sin(x);
}

double my_sin_int(double x){
	return (-1)*cos(x);
}

/*
 * cos functions
 */
double my_cos(double x){
	return cos(x);
}

double my_cos_diff(double x){
	return (-1)*sin(x);
}


/*
 * tan functions
 */
double my_tan(double x){
	return tan(x);
}

double my_tan_diff(double x){
	return 1/(pow(cos(x),2));
}

double my_tan_int(double x){
	return (-1)*log(cos(x));
}

/*
 * 1_x functions
 */

double my_1_x(double x){
	return 1/x;
}

double my_1_x_diff(double x){
	return -1/pow(x,2);
}

double my_1_x_int(double x){
	return log(x);
}


