package priyesh.Crowdsense2.Controllers;

import jakarta.annotation.Generated;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoController {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    }
    private CascadeClassifier faceDetector;
    private int totalFacesDetected = 0;

    @PostConstruct
    public void init() {
        faceDetector = new CascadeClassifier("src/main/resources/haarcascade_frontalface_default.xml");
    }
    @GetMapping("/video-feed")
    public void videoFeed(HttpServletResponse response) throws Exception{
        response.setContentType("multipart/x-mixed-replace; boundary=frame");
        Mat frame = new Mat();
        VideoCapture videoCapture = new VideoCapture(0);

        while (true){
            if (videoCapture.read(frame)) {
                totalFacesDetected =  detectAndDrawFaces(frame);

                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".jpg", frame, matOfByte);

                byte[] byteArray = matOfByte.toArray();
                response.getOutputStream().write(("--frame\r\n" +
                        "Content-Type: image/jpeg\r\n\r\n").getBytes());
                response.getOutputStream().write(byteArray);
                response.getOutputStream().write("\r\n\r\n".getBytes());
                response.flushBuffer();
            }
        }
    }
    private int detectAndDrawFaces(Mat frame) {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayFrame, faces);
        int faceCount = 0;
        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);
            faceCount++;
        }
        return faceCount;
    }
    @GetMapping("/face-count")
    @ResponseBody
    public String getFaceCount(){
        return "{ \"totalFaces\": " + totalFacesDetected + " }";

    }



}
