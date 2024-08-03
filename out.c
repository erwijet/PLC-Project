#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

char* __jott_concat(char* s1, char* s2) {
  strcat(s1, s2);
  return s1;
}
void testPrintLoop(int x){while(x>0){
printf("%d",x); x = x-1; 
} 
}
char* testConcatLoop(char* y){int x;
char* output;
x = strlen(y)-1; output = y; while(x>0){
output = __jott_concat(output,y); x = x-1; 
} return output;
}
void testIf(double d, int x){if(d>5.1){
printf("%s","Hi"); 
}else if(d>0.1){
printf("%d",x); 
}else{
printf("%f",3.2*4.0); 
} 
}
int testIfLoop(int x){int output;
bool __switch;
output = 1; __switch = true; while(x>0){
if(__switch){
output = output*2; __switch = false; 
}else{
__switch = true; 
} x = x-1; 
} return output;
}
void foo(){printf("%s","ran foo"); 
}
void testFuncCallLoop(int x){while(x>0){
foo(); x = x-2; 
} 
}
int main(void){char* str;
int intI;
testPrintLoop(5); str = "a1"; printf("%s",testConcatLoop(str)); testIf(1.1,3); intI = testIfLoop(3); printf("%d",intI); testFuncCallLoop(5); 
return 1;
}
