#include <iostream>
#include <omp.h>
#include <vector>
#include <algorithm>

using namespace std;

// Function to find minimum value using parallel reduction
int parallelMin(const vector<int>& values) {
    int min_val = values[0];
    #pragma omp parallel for reduction(min:min_val)
    for (size_t i = 1; i < values.size(); ++i) {
        min_val = min(min_val, values[i]);
    }
    return min_val;
}

// Function to find maximum value using parallel reduction
int parallelMax(const vector<int>& values) {
    int max_val = values[0];
    #pragma omp parallel for reduction(max:max_val)
    for (size_t i = 1; i < values.size(); ++i) {
        max_val = max(max_val, values[i]);
    }
    return max_val;
}

// Function to find sum of values using parallel reduction
int parallelSum(const vector<int>& values) {
    int sum = 0;
    #pragma omp parallel for reduction(+:sum)
    for (size_t i = 0; i < values.size(); ++i) {
        sum += values[i];
    }
    return sum;
}

// Function to find average of values using parallel reduction
double parallelAverage(const vector<int>& values) {
    int sum = parallelSum(values);
    return static_cast<double>(sum) / values.size();
}

int main() {
    vector<int> values = {5, 9, 3, 7, 2, 8, 4, 6, 1};

    // Find minimum value
    int min_val = parallelMin(values);
    cout << "Minimum value: " << min_val << endl;

    // Find maximum value
    int max_val = parallelMax(values);
    cout << "Maximum value: " << max_val << endl;

    // Find sum of values
    int sum = parallelSum(values);
    cout << "Sum of values: " << sum << endl;

    // Find average of values
    double avg = parallelAverage(values);
    cout << "Average of values: " << avg << endl;

    return 0;
}
