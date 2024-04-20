#include <iostream>
#include <omp.h>
using namespace std;

int main() {
   

    #pragma omp parallel
    {
        cout<<"hello"<<endl;
    }

    return 0;
}

/*
g++ -o binary  hello_openmp.cpp -fopenmp
./binary

output:

hellohello
hello
hello
hello
hello
hello
hello
hello
hello

hello
hello
*/