#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#define N 1000
#define LOG 0 // Do you want log?

void printMatrix(int ** m);
void printMatrixLong(long ** m);

int ** memoryAllocation();
void freeMemory(int ** m);

long ** memoryAllocationLong();
void freeMemoryLong(long ** m);

long abss(long a);

int main(void){

    long ** A = memoryAllocationLong();
    long ** B = memoryAllocationLong();
    long ** C = memoryAllocationLong();

    for (int i = 0; i < N; ++i){
        for (int j = 0; j < N; ++j){
            A[i][j] = 2 * i + j;
            B[i][j] = 2 * i - j;
            C[i][j] = 0;
        }
    }

    long sum = 0;

    for (int i = 0; i < N; ++i){
        for (int j = 0; j < N; ++j){
            for (int k = 0; k < N; ++k){
                
                if (LOG){
                    if (B[k][j] != 0 && abss(A[i][k]) * abss(B[k][j]) > INT_MAX){
                        printf("OVERFLOW IN MULTIPLICATION: A[%d][%d]: %ld, B[%d][%d]: %ld\n", i, k, A[i][k], k, j, B[k][j]);
                    }
                    if (C[i][j] > INT_MAX - (long)A[i][k] * B[k][j]){
                        printf("OVERFLOW IN INDEX OF C: %ld, INT_MAX: %d, MULTIPLICATION: %ld\n", C[i][j], INT_MAX, A[i][k] * B[k][j]);
                    }
                }
                
                C[i][j] += A[i][k] * B[k][j];
            }

            sum += C[i][j];
        }
    }

    printf("\nThe checksum is: %ld\n", sum);

    if (N == 4){
        printMatrixLong(C);
    }

    freeMemoryLong(A);
    freeMemoryLong(B);
    freeMemoryLong(C);

    return 0;
}

void printMatrix(int ** m){
    for (int i = 0 ; i < N; ++i){
        for (int j = 0; j < N; ++j){
            printf("%d ", m[i][j]);
        }

        printf("\n");
    }
}

void printMatrixLong(long ** m){
    for (int i = 0 ; i < N; ++i){
        for (int j = 0; j < N; ++j){
            printf("%ld ", m[i][j]);
        }

        printf("\n");
    }
}

int ** memoryAllocation(){
    int ** matrix = (int **)malloc(sizeof(int *) * N);

    for (int i = 0; i < N; ++i)
        matrix[i] = (int *)malloc(sizeof(int) * N);

    return matrix;
}

long ** memoryAllocationLong(){
    long ** matrix = (long **)malloc(sizeof(long *) * N);

    for (int i = 0; i < N; ++i)
        matrix[i] = (long *)malloc(sizeof(long) * N);

    return matrix;
}

void freeMemory(int ** m){
    for (int i = 0; i < N; ++i){
        free(m[i]);
    }

    free(m);
}

void freeMemoryLong(long ** m){
    for (int i = 0; i < N; ++i){
        free(m[i]);
    }

    free(m);
}

long abss(long a){
    return a < 0 ? -a : a;
}