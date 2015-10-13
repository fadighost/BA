import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.jar.Attributes.Name;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import intel.rssdk.PXCMHandData.GestureStateType;
import intel.rssdk.PXCMCapture;
import intel.rssdk.PXCMCapture.StreamType;
import intel.rssdk.PXCMCaptureManager;
import intel.rssdk.PXCMHandConfiguration;
import intel.rssdk.PXCMHandConfiguration.GestureHandler;
import intel.rssdk.PXCMHandData;
import intel.rssdk.PXCMHandData.AlertType;
import intel.rssdk.PXCMHandData.GestureData;
import intel.rssdk.PXCMHandModule;
import intel.rssdk.PXCMPoint3DF32;
import intel.rssdk.PXCMPointF32;
import intel.rssdk.PXCMSenseManager;
import intel.rssdk.PXCMVideoModule;
import intel.rssdk.pxcmStatus;

public class GestureViewer  {
	
	public static void main(String s[]) {
	System.out.println("Starting hand tracker.");
	
	// create the sense manager and enable the streams,
	// get the hand module, and initialize the pipeline
	PXCMSenseManager senseManager =PXCMSenseManager.CreateInstance();
	senseManager.EnableStream(PXCMCapture.StreamType.STREAM_TYPE_COLOR,640,480,30);
	senseManager.EnableHand(null);
	PXCMHandModule handModule = senseManager.QueryHand();
	senseManager.Init();
	
	// configure the hand module, we want all gestures
	// we also want a PXCHandData instance which we'll
	// later query for gesture data. We also enable
	// alerts if a hand is detected or lost
	PXCMHandData handData = handModule.CreateOutput();
	//PXCMHandData.GestureData gestureData = new PXCMHandData.GestureData();
	PXCMHandData.GestureData gestureData = new PXCMHandData.GestureData();
	PXCMHandConfiguration handConfig = handModule.CreateActiveConfiguration();
	
	handConfig.DisableAllGestures();
	
	handConfig.EnableGesture("thumb_up");
	handConfig.EnableGesture("thumb_down");
	handConfig.EnableGesture("swipe_right");
	handConfig.EnableGesture("swipe_left");

	//handConfig.EnableAllAlerts();
	//handConfig.EnableAllGestures(); 
	//handConfig.EnableAlert(AlertType.ALERT_HAND_DETECTED);
	//handConfig.EnableAlert(AlertType.ALERT_HAND_NOT_DETECTED);

				
	handConfig.ApplyChanges();
		
	

	PXCMCaptureManager captureMgr = senseManager.QueryCaptureManager();
	captureMgr.FilterByDeviceInfo("RealSense",null, 0);
	pxcmStatus sts = senseManager.EnableHand(null);
	sts = senseManager.Init();
							 
	handConfig.Update();
	
		if (sts.compareTo(pxcmStatus.PXCM_STATUS_NO_ERROR) >= 0) {
			senseManager.Init();
							
		//	for (int nframes = 0; nframes < 3000; nframes++) {
		//	System.out.println("Frame # " + );
			sts = senseManager.AcquireFrame(true);
			if (sts.compareTo(pxcmStatus.PXCM_STATUS_NO_ERROR) < 0)
			
	
				while (senseManager.AcquireFrame(true).isSuccessful()) {
					
				// Retrieve current hand tracking results
				handData = handModule.CreateOutput();
				handData.Update();

				//getting the 3 gestures
				
				if (handData.IsGestureFired("thumb_up", gestureData)) {
				System.out.println("thumb up!!");	
				}
				
				else if (handData.IsGestureFired("thumb_down", gestureData)) {
					System.out.println("thumb down!!");	
					}
				else if (handData.IsGestureFired("wave", gestureData)) {
					System.out.println("wave!!");	
					}
				else if (handData.IsGestureFired("swipe_right", gestureData)) {
					System.out.println("swipe right!!");	
					}
				else if (handData.IsGestureFired("swipe_left", gestureData)) {
					System.out.println("swipe left!!");	break;
					}

				PXCMHandData.IHand hand = new PXCMHandData.IHand();
				sts = handData.QueryHandData(PXCMHandData.AccessOrderType.ACCESS_ORDER_NEAR_TO_FAR,0,hand);
				
				//added for positions
			/*	if (sts.compareTo(pxcmStatus.PXCM_STATUS_NO_ERROR) >= 0) {
					
					
					PXCMPointF32 image = hand.QueryMassCenterImage();
					PXCMPoint3DF32 world = hand.QueryMassCenterWorld();
					
					System.out.print("   Image Position: (" + image.x + ","+ image.y + ")");
					System.out.println("   World Position: (" + world.x + ","+ world.y + "," + world.z + ")");
					
					
				}
		
				// gestures
				int ngestures = handData.QueryNumberOfHands();
				System.out.println("# of hands is "+ ngestures);
		*/				

				senseManager.ReleaseFrame();
				}	
		//	}
			handData.close();
		senseManager.close();
		
		}
	}
	
}