import multiprocessing

def parallel_bubble_sort(arr):
    n = len(arr)
    for i in range(n):
        for j in range(0, n-i-1):
            if arr[j] > arr[j+1]:
                arr[j], arr[j+1] = arr[j+1], arr[j]

def merge(arr, left, mid, right):
    # Merge two sorted subarrays into one sorted array
    n1 = mid - left + 1
    n2 = right - mid

    L = arr[left:left + n1]
    R = arr[mid + 1:mid + 1 + n2]

    i = j = 0
    k = left

    while i < n1 and j < n2:
        if L[i] <= R[j]:
            arr[k] = L[i]
            i += 1
        else:
            arr[k] = R[j]
            j += 1
        k += 1

    while i < n1:
        arr[k] = L[i]
        i += 1
        k += 1

    while j < n2:
        arr[k] = R[j]
        j += 1
        k += 1

def parallel_merge_sort(arr):
    if len(arr) > 1:
        mid = len(arr) // 2
        left_arr = arr[:mid]
        right_arr = arr[mid:]

        # Create processes to sort left and right halves in parallel
        left_process = multiprocessing.Process(target=parallel_merge_sort, args=(left_arr,))
        right_process = multiprocessing.Process(target=parallel_merge_sort, args=(right_arr,))

        left_process.start()  # Start sorting left half
        right_process.start()  # Start sorting right half

        left_process.join()  # Wait for left sorting to complete
        right_process.join()  # Wait for right sorting to complete

        # Merge the sorted halves
        merge(arr, 0, mid - 1, len(arr) - 1)

if __name__ == "__main__":
    arr = [64, 34, 25, 12, 22, 11, 90]
    
    print("Original array:", arr)

    # Parallel bubble sort
    parallel_bubble_sort(arr)
    print("Sorted array using parallel bubble sort:", arr)

    # Parallel merge sort
    parallel_merge_sort(arr)
    print("Sorted array using parallel merge sort:", arr)
