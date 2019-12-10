//based on the code on https://www.ylmzcmlttn.com/2019/06/07/bgr-to-rgb-with-cuda-cuda-and-opencv/

#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <string>
#include <cuda.h>
#include <cuda_runtime.h>
#include <cuda_runtime_api.h>
#include <cuda_device_runtime_api.h>
#include <stdio.h>
#include <opencv2\opencv.hpp>
#include <opencv2\core.hpp>
#include <opencv2\highgui.hpp>
#include <opencv2\imgproc.hpp>
#include <iostream>

using namespace cv;
using namespace std;

__global__ void sepia(uint8_t* input, int width, int height, int colorWidthStep)
{
	//2D Index of current thread
	const int xIndex = blockIdx.x * blockDim.x + threadIdx.x;
	const int yIndex = blockIdx.y * blockDim.y + threadIdx.y;

	//Only valid threads perform memory I/O
	if ((xIndex < width) && (yIndex < height))
	{
		//Location of colored pixel in input
		const int color_tid = yIndex * colorWidthStep + (3 * xIndex);
		//const uint8_t t = input[color_tid + 0];
		int b = input[color_tid + 0];
		int g = input[color_tid + 1];
		int r = input[color_tid + 2];
		int tr = (int)(r * 0.393 + g * 0.769 + b * 0.189);
		int tg = (int)(r * 0.349 + g * 0.686 + b * 0.168);
		int tb = (int)(r * 0.272 + g * 0.534 + b * 0.131);
		if (tr > 255) {
			r = 255;
		}else {
			r = tr;
		}
		if (tg > 255) {
			g = 255;
		}
		else {
			g = tg;
		}
		if (tb > 255) {
			b = 255;
		}
		else {
			b = tb;
		}
		input[color_tid + 0] = b;
		input[color_tid + 1] = g;
		input[color_tid + 2] = r;
	}
}

inline void mono_red(const Mat& input) {
	const int Bytes = input.step * input.rows;
	uint8_t* d_input;
	cudaEvent_t start, stop;
	float time;
	cudaEventCreate(&start);
	cudaEventCreate(&stop);
	cudaMalloc((uint8_t**)&d_input, sizeof(uint8_t) * Bytes);
	cudaMemcpy(d_input, input.data, sizeof(uint8_t) * Bytes, cudaMemcpyHostToDevice);
	//cudaMemcpy(d_output,output.ptr(),Bytes,cudaMemcpyHostToDevice);
	dim3 block(4, 4);
	dim3 grid((input.cols + block.x - 1) / block.x, (input.rows + block.y - 1) / block.y);
	cudaEventRecord(start, 0);
	sepia << <grid, block >> > (d_input, input.cols, input.rows, input.step);
	cudaEventRecord(stop, 0);
	cudaEventSynchronize(stop);
	cudaMemcpy(input.data, d_input, sizeof(uint8_t) * Bytes, cudaMemcpyDeviceToHost);
	cudaFree(d_input);
	cudaEventElapsedTime(&time, start, stop);
	printf("Time for the kernel: %f ms\n", time);
	cudaEventDestroy(start);
	cudaEventDestroy(stop);
	//cudaDeviceSynchronize();
}

int main(int argc, char const* argv[]) {

	printf("Program is started\n");
	Mat image = imread("lena.jpg");
	//Mat image(image_bgr.rows, image_bgr.cols, CV_8UC3);
	//cvtColor(image_bgr, image, COLOR_BGR2RGB);
	//Mat image_out(image.rows, image.cols, CV_8UC3);

	mono_red(image);


	imwrite("lena_sepia_CUDA.jpg", image);
	system("pause");


	return 0;
}
