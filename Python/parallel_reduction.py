import multiprocessing

def parallel_min(array):
    chunk_size = max(len(array) // multiprocessing.cpu_count(), 1)
    chunks = [array[i:i+chunk_size] for i in range(0, len(array), chunk_size)]
    with multiprocessing.Pool() as pool:
        mins = pool.map(min, chunks)
    return min(mins)

def parallel_max(array):
    chunk_size = max(len(array) // multiprocessing.cpu_count(), 1)
    chunks = [array[i:i+chunk_size] for i in range(0, len(array), chunk_size)]
    with multiprocessing.Pool() as pool:
        maxes = pool.map(max, chunks)
    return max(maxes)

def parallel_sum(array):
    chunk_size = max(len(array) // multiprocessing.cpu_count(), 1)
    chunks = [array[i:i+chunk_size] for i in range(0, len(array), chunk_size)]
    with multiprocessing.Pool() as pool:
        sums = pool.map(sum, chunks)
    return sum(sums)

def parallel_average(array):
    return parallel_sum(array) / len(array)

if __name__ == "__main__":
    array = [4, 2, 7, 1, 9, 5, 3, 8, 6]
    print("Array:", array)
    print("Minimum:", parallel_min(array))
    print("Maximum:", parallel_max(array))
    print("Sum:", parallel_sum(array))
    print("Average:", parallel_average(array))
