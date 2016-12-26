#include "VehicleDetector.h"

int VehicleDetector::trainCascade(string filename) {
	if (!car_cascade.load("cars.xml")) {
		return -1;
	} else
		return 1;
}

void VehicleDetector::detectAndDisplay(string filename) {
	VideoCapture cap(filename);
	if (!cap.isOpened()) return;
	Mat frame;
	//read video frame per frame
	int framecnt = 0;
	int total = 0;
	int step = 100;

	while (true) {
		cap.read(frame);

		if (!frame.empty()) {
			//resize
			resize(frame, frame, Size(640, 360), 0, 0, INTER_LINEAR);
//			if (total == 0 || total > step) {
//				imwrite(to_string(total) + ".jpg", frame);
//				step += step;
//			}
			//detect cars
			vector<Rect> newregions;
			detectROI(frame, newregions);
			vector<Rect> rectangles;
			for (size_t i = 0; i < newregions.size(); i++) {
				if (isNewRoi(newregions[i].x, newregions[i].y, rectangles)) {
					rectangles.push_back(newregions[i]);
				}
			}
			vector<Rect> groundtruth = getGroundtruth(total);
			evaluateQuality(frame, groundtruth, rectangles);
			//draw rects
			for (size_t i = 0; i < rectangles.size(); i++) {
				rectangle(frame, rectangles[i], Scalar(0, 255, 0), 2);
			}
			//refresh after 30 frames
			framecnt++;
			total++;
			if (framecnt > 30) {
				framecnt = 0;
				rectangles.clear();
			}
			//show frame
			imshow("Car Detect", frame);
		}

		char key = waitKey(1);
		if (key == 'q')
			break;
	}
	cap.release();
}

vector<Rect> VehicleDetector::getGroundtruth(int frm) {
	vector<Rect> res;
	string filename;
	if (frm >= 0 && frm < 31) {
		filename = "0.txt";
	} else if (frm >= 31 && frm < 62) {
		filename = "31.txt";
	} else if (frm >= 62 && frm < 93) {
		filename = "62.txt";
	} else if (frm >= 93 && frm < 124) {
		filename = "93.txt";
	} else if (frm >= 124 && frm < 155) {
		filename = "124.txt";
	} else if (frm >= 155 && frm < 186) {
		filename = "155.txt";
	} else if (frm >= 186 && frm < 201) {
		filename = "186.txt";
	} else if (frm >= 201 && frm < 217) {
		filename = "201.txt";
	} else if (frm >= 217 && frm < 248) {
		filename = "217.txt";
	} else if (frm >= 248 && frm < 279) {
		filename = "248.txt";
	} else if (frm >= 279 && frm < 310) {
		filename = "279.txt";
	} else
		filename = "310.txt";
	ifstream input(filename);
	string currline;
	while (getline(input, currline)) {
		vector<string> line_elements;
		stringstream temp(currline);
		string item;
		while (getline(temp, item, ' ')) {
			line_elements.push_back(item);
		}
		if (line_elements.size() > 3) {
			Rect rect = Rect(atoi(line_elements[0].c_str()),
					atoi(line_elements[1].c_str()),
					atoi(line_elements[2].c_str()),
					atoi(line_elements[3].c_str()));
			res.push_back(rect);
		}
	}
	input.close();
	return res;
}

void VehicleDetector::evaluateQuality(Mat frame, vector<Rect> groundtruth, vector<Rect> rects) {
	int tp = 0;
	for (size_t i = 0; i < groundtruth.size(); i++) {
		//rectangle(frame, groundtruth[i], Scalar(255, 0, 0), 2);
		for (size_t j = 0; j < rects.size(); j++) {
			if ((groundtruth[i] & rects[j]).area() > 0) {
				tp++;
			}
		}
	}
	int fp = rects.size() - tp;
	if (fp < 0) fp = 0;
	int fn = groundtruth.size() - tp;
	if (fn < 0) fn = 0;
	int tpfn = tp + fn;
	int tpfp = tp + fp;
	//cout << tp << " " << tpfn << "\n";
	double recall = tpfn != 0 ? ((double)tp / tpfn) : 0;
	double precision = tpfp != 0 ? ((double)tp / tpfp) : 0;
	cout << recall << " " << precision << "\n";
	//putText(frame, "recall: " + to_string((recall)) + " precision: " + to_string((precision)), Point(10, 10), 1, 1, Scalar(0,0,255), 2);
}

bool VehicleDetector::isCarLight(Mat frame) {
	Mat img_bw;
	threshold(frame, img_bw, 0, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
	Scalar temp = mean(img_bw);
	float mean = temp.val[0];
	//cout << mean << "\n";
	return mean > 165;
}

void VehicleDetector::detectROI(Mat frame, vector<Rect> &objs) {
	//resize
	double scaleDown = 2;
	Size size = frame.size();
	int frameHeight = size.height;
	int frameWidth = size.width;
	resize(frame, frame, Size(frameWidth / scaleDown, frameHeight / scaleDown),
			0, 0, INTER_LINEAR);
	size = frame.size();
	frameHeight = size.height;
	frameWidth = size.width;
	//detect roi
	vector<Rect> cars;
	Mat frame_gray;
	cvtColor(frame, frame_gray, CV_BGR2GRAY);
	equalizeHist(frame_gray, frame_gray);
	car_cascade.detectMultiScale(frame_gray, cars, 1.1, 0, 0, Size(30, 30), Size(150, 150));
	int minY = (int) (frameHeight * 0.1);
	//int maxY = (int) (frameHeight * 0.66);
	//for each roi
	for (size_t i = 0; i < cars.size(); i++) {
		Mat roiImage = frame_gray(Range(cars[i].y, cars[i].y + cars[i].height),
				Range(cars[i].x, cars[i].x + cars[i].width));
		if (cars[i].y > minY) {
			//double diffX = diffLeftRight(roiImage);
			//double diffY = diffUpDown(roiImage);
			//cout << diffX << " : " << diffY << "\n";
			//if (diffX > 1500 && diffX < 4000 && diffY > 3500) {
			if (/*diffX > 1500 && diffX < 4000 && diffY > 3000 &&*/ isCarLight(roiImage)) {
				objs.push_back(
						Rect(cars[i].x * scaleDown, cars[i].y * scaleDown,
								cars[i].width * scaleDown,
								cars[i].height * scaleDown));
			}
		}
	}
}

bool VehicleDetector::isNewRoi(int rx, int ry, vector<Rect> rectangles) {
	for (size_t i = 0; i < rectangles.size(); i++) {
		if (abs(rectangles[i].x - rx) < 40 && abs(rectangles[i].y - ry) < 40) {
			return false;
		}
	}
	return true;
}
