#include "VehicleDetector.h";

int main(int argc, char** argv) {
	VehicleDetector detector;
	detector.trainCascade("cars.xml");
	detector.detectAndDisplay("video1.mp4");
	return 0;
}
