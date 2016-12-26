#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include <iostream>
#include <fstream>
using namespace cv;
using namespace std;

class VehicleDetector{
public:
	int trainCascade(string filename);
	void detectAndDisplay(string filename);
private:
	CascadeClassifier car_cascade;
	vector<Rect> getGroundtruth(int frm);
	bool isCarLight(Mat frame);
	bool isNewRoi(int rx, int ry, vector<Rect> rectangles);
	void detectROI(Mat frame, vector<Rect> &objs);
	void evaluateQuality(Mat frame, vector<Rect> groundtruth, vector<Rect> rects);
};
