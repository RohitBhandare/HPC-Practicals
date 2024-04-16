import numpy as np
import multiprocessing

def multiply_row_col(row, col):
    return sum(x * y for x, y in zip(row, col))

def parallel_matrix_multiply(matrix1, matrix2):
    result = np.zeros((len(matrix1), len(matrix2[0])))
    pool = multiprocessing.Pool()

    for i in range(len(matrix1)):
        for j in range(len(matrix2[0])):
            result[i][j] = pool.apply(multiply_row_col, args=(matrix1[i], [row[j] for row in matrix2]))

    pool.close()
    pool.join()
    return result

if __name__ == "__main__":
    matrix1 = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
    matrix2 = np.array([[9, 8, 7], [6, 5, 4], [3, 2, 1]])

    result = parallel_matrix_multiply(matrix1, matrix2)
    print("Matrix 1:")
    print(matrix1)
    print("Matrix 2:")
    print(matrix2)
    print("Result:")
    print(result)
