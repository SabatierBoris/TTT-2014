package TTT.PC.models.odometry;

import java.util.EventListener;

import TTT.commons.navigation.Pose;

public interface OdometryListener extends EventListener {
	public void newPose(Pose pose);
}

