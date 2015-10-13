
public class PXCMSenseManager {
	// Create a SenseManager instance
	PXCMSenseManager sm=PXCMSenseManager.CreateInstance();
	 
	// Enable a video stream
	sm.EnableStream(PXCMCapture.StreamType.STREAM_TYPE_COLOR,0,0,0);
	 
	// Initialize
	sm.Init();
	 
	// Stream Data
	while (sm.AcquireFrame(true).isSuccessful()) {
	 
	   // Get the sample data
	   PXCMCapture.Sample sample=sm.QuerySample();
	   .... // process image sample.color
	 
	   // Resume next frame processing
	   sm.ReleaseFrame();
	}
	 
	// Clean up
	sm.close();
}
