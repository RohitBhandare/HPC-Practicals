#include <iostream>
#include <omp.h>

int main() {
    // Set number of threads
    omp_set_num_threads(4); // You can change the number of threads as needed

    #pragma omp parallel
    {
        int thread_id = omp_get_thread_num();
        std::cout << "Hello, World! from thread " << thread_id << std::endl;
    }

    return 0;
}
