package com.example.android.opengl.vr_content;

import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

/**
 * A thing willing to control near and far clipping planes so as to create the desired
 * perspective view of a scene.
 */
public class ScenePerspective {

    private static final float FIELD_OF_VIEW_DEGREES = 90.0f;

    public float[] calculateProjectionTransform(
            final Viewpoint viewpoint, final Sphere sceneSphere, float screenAspect) {
        final float far = calculateFarPlane(viewpoint, sceneSphere, screenAspect);
        final float near = calculateNearPlane(far, sceneSphere);
        return TransformFactory.perspective(FIELD_OF_VIEW_DEGREES, screenAspect, near, far);
    }

    /** Calculates a value for the FAR clipping plane, by positioning it snugly to match the
     * farthest extent of the scene contents - as implied by the scene's sphere.
     */
    private float calculateFarPlane(
            final Viewpoint viewpoint, final Sphere sceneSphere, float screenAspect) {
        final XYZf offsetFromSphereCentreToFarPlane =
                viewpoint.getLineOfSightVector().vectorScaledToLength(sceneSphere.getRadius());
        final XYZf pointOnFarPlane = sceneSphere.getCentre().plus(offsetFromSphereCentreToFarPlane);
        final XYZf vectorFromCameraToFarPlaneSamplePoint =
                pointOnFarPlane.minus(viewpoint.getViewersPosition());
        float perpendicularDistanceFromCameraToFarPlane =
                vectorFromCameraToFarPlaneSamplePoint.dotProduct(
                        viewpoint.getLineOfSightVector());
        final float far = perpendicularDistanceFromCameraToFarPlane;
        return far;
    }

    /** Calculates the NEAR clipping plane by trying to position it snugly at the nearest
     * extent of the scene contents, as implied by the scene's sphere. However if this would place
     * the NEAR plane behind the camera it is prevented from doing so, by not allowing to be set
     * to a value less than zero. I.e coincident with the camera in this case.
     */
    private float calculateNearPlane(final float far, final Sphere sceneSpere) {
        final float nearestEdgeOfSphere = far - 2 * sceneSpere.getRadius();
        final float near = Math.max(nearestEdgeOfSphere, 0);
        return near;
    }
}
