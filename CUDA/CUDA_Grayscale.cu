<<<<<<< HEAD
//based on code found at https://www.ylmzcmlttn.com/2019/06/07/bgr-to-rgb-with-cuda-cuda-and-opencv/
=======
//based on the code on https://www.ylmzcmlttn.com/2019/06/07/bgr-to-rgb-with-cuda-cuda-and-opencv/

>>>>>>> bb60c310d639cf6f0b4edc8b2cfcc10bce86bbb1
#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <string>
#include <cuda.h>
#include <stdio.h>
#include <opencv2\opencv.hpp>
#include <opencv2\core.hpp>
#include <opencv2\highgui.hpp>
#include <opencv2\imgproc.hpp>
#include <iostream>
#include <time.h>

using namespace cv;
using namespace std;

__global__ void rgb_to_gray_kernel(uint8_t* input, int width, int height, int colorWidthStep)
{
	//2D Index of current thread
	const int xIndex = blockIdx.x * blockDim.x + threadIdx.x;
	const int yIndex = blockIdx.y * blockDim.y + threadIdx.y;

	//Only valid threads perform memory I/O
	if ((xIndex < width) && (yIndex < height))
	{
		//Location of colored pixel in input
		const int color_tid = yIndex * colorWidthStep + (3 * xIndex);
		int r = input[color_tid + 0];
		int g = input[color_tid + 1];
		int b = input[color_tid + 2];
		input[color_tid + 0] = b * 0.07 + g * 0.71 + r * 0.21;
		input[color_tid + 1] = b * 0.07 + g * 0.71 + r * 0.21;
		input[color_tid + 2] = b * 0.07 + g * 0.71 + r * 0.21;
	}
}

inline void rgb_to_gray(const Mat& input) {
	const int Bytes = input.step * input.rows;
	uint8_t* d_input;
	cudaEvent_t start, stop;
	float time;
	cudaEventCreate(&start);
	cudaEventCreate(&stop);
	cudaMalloc((uint8_t**)&d_input, sizeof(uint8_t) * Bytes);
	cudaMemcpy(d_input, input.data, sizeof(uint8_t) * Bytes, cudaMemcpyHostToDevice);
	dim3 block(4, 4);
	dim3 grid((input.cols + block.x - 1) / block.x, (input.rows + block.y - 1) / block.y);
	cudaEventRecord(start, 0);
	rgb_to_gray_kernel << <grid, block >> > (d_input, input.cols, input.rows, input.step);
	cudaEventRecord(stop, 0);
	cudaEventSynchronize(stop);
	cudaMemcpy(input.data, d_input, sizeof(uint8_t) * Bytes, cudaMemcpyDeviceToHost);
	cudaFree(d_input);
	cudaEventElapsedTime(&time, start, stop);
	printf("Time for the kernel: %f ms\n", time);
	cudaEventDestroy(start);
	cudaEventDestroy(stop);
	cudaDeviceSynchronize();
}

int main(int argc, char const* argv[]) {

	printf("Program is started\n");
	Mat image = imread("lena.jpg");
	
	rgb_to_gray(image);

	imwrite("lena_grayscale_CUDA.jpg", image);
	system("pause");

	return 0;
}
