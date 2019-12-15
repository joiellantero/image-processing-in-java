//based on code found at https://www.ylmzcmlttn.com/2019/06/07/bgr-to-rgb-with-cuda-cuda-and-opencv/
#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <string>
#include <string.h>
#include <cuda.h>
#include <stdio.h>
#include <opencv2\opencv.hpp>
#include <opencv2\core.hpp>
#include <opencv2\highgui.hpp>
#include <opencv2\imgproc.hpp>
#include <dirent.h>
#include <iostream>
#include <cstring>
#include <sys/types.h>
#include <stdlib.h>

__global__ void rgb_to_gray_kernel(uint8_t* input, int width, int height, int colorWidthStep);

using namespace cv;
using namespace std;

__global__ void rgb_to_gray_kernel(uint8_t* input, int width, int height, int colorWidthStep)
{
	const int xIndex = blockIdx.x * blockDim.x + threadIdx.x;
	const int yIndex = blockIdx.y * blockDim.y + threadIdx.y;

	if ((xIndex < width) && (yIndex < height))
	{
		const int color_tid = yIndex * colorWidthStep + (3 * xIndex);
		int r = input[color_tid + 0];
		int g = input[color_tid + 1];
		int b = input[color_tid + 2];
		input[color_tid + 0] = b * 0.07 + g * 0.71 + r * 0.21;
		input[color_tid + 1] = b * 0.07 + g * 0.71 + r * 0.21;
		input[color_tid + 2] = b * 0.07 + g * 0.71 + r * 0.21;
	}
}

float rgb_to_gray(const Mat& input) {
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
	rgb_to_gray_kernel << <grid, block>> > (d_input, input.cols, input.rows, input.step);
	cudaEventRecord(stop, 0);
	cudaEventSynchronize(stop);
	cudaMemcpy(input.data, d_input, sizeof(uint8_t) * Bytes, cudaMemcpyDeviceToHost);
	cudaFree(d_input);
	cudaEventElapsedTime(&time, start, stop);
	cudaEventDestroy(start);
	cudaEventDestroy(stop);
	cudaDeviceSynchronize();
	return time;
}

int main()
{
	struct dirent* de;  // Pointer for directory entry 
	int i = 0;
	float time = 0;
	FILE* fp;

	// opendir() returns a pointer of DIR type.  
	DIR* dr = opendir("./test_images/");
	printf("Program has started\n");
	if (dr == NULL)  // opendir returns NULL if couldn't open directory 
	{
		printf("Could not open current directory");
		return 0;

	}
	while ((de = readdir(dr)) != NULL) {
		if (i > 1) {
			string name(de->d_name);
			string path("./test_images/");
			string new_path("./processed_images/");
			path.append(name);
			new_path.append(name);
			cout << path << endl;
			Mat image = imread(path);

			time = rgb_to_gray(image);

			imwrite(new_path, image);
			fp = fopen("CUDA_grayscale.txt", "a");
			fprintf(fp, "%f\n", time / 1000);
			fclose(fp);
		}
		i++;
	}
	closedir(dr);
	printf("End.");
	system("pause");
}
