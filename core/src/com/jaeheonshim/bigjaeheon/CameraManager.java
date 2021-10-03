package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraManager {
    public static final float CAM_LERP = 0.4f;

    public static final float MAX_WIDTH = 60;
    public static final float MAX_HEIGHT = 50;
    public static final float ZOOM_WIDTH = 30;
    public static final float ZOOM_HEIGHT = 30;

    private OrthographicCamera camera;
    private Viewport viewport;

    public CameraManager() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(ZOOM_WIDTH, ZOOM_HEIGHT, MAX_WIDTH, MAX_HEIGHT, camera);

        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void followPosition(Vector2 position) {
        Vector2 cameraDelta = new Vector2(MathUtils.clamp(position.x, viewport.getWorldWidth() / 2f, MAX_WIDTH - viewport.getWorldWidth() / 2f) - camera.position.x, MathUtils.clamp(position.y, viewport.getWorldHeight() / 2f, MAX_HEIGHT - viewport.getWorldHeight() / 2f) - camera.position.y);
        cameraDelta.scl(CAM_LERP);
        camera.position.add(new Vector3(cameraDelta.x, cameraDelta.y, camera.position.z));

        camera.update();
    }
}
