package com.example.android.opengl.vr_content;

import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

/**
 * A service that knows about the transformation mathematics for perspective projections
 * of a world, from the viewpoint of a camera. In other words if you have vertices in a world,
 * and you know where the camera is in that world, and in which direction it is pointing, then
 * the function provided here, will give you a 4*4 transformation matrix to apply to the vertices
 * that will project the vertices onto a 2D screen as a scene with perspective, as viewed by that
 * camera.
 *
 * The literature speaks of Model-View-Projection (usually abbreviated to mvp) transforms. This
 * function is taking care of the last two bits - the view (camera) transform, followed by a
 * perspective transform.
 */
public class ViewProjectionCalculator {

    private static final float FIELD_OF_VIEW_DEGREES = 90.0f; // commonly used rule of thumb

    // What is the minimum NEAR plane distance value we will use - expressed as a proportion
    // of the FAR plane distance we have chosen.
    private static final float NEAR_PLANE_MINIMUM_FACTOR = 0.10f;

    // What is the minimum separation we want to maintain between the NEAR and FAR plane distances,
    // expressed as a minimum value for the far plane, as a proportion of the NEAR plane distance.
    private static final float FAR_PLANE_MINIMUM_FACTOR = 10.0f;

    /**
     * Decides on the view-projection parameterisation required and provides the corresponding
     * transformation matrix. See also {@link TransformFactory}.
     * @param viewingAxis Combines the position of the camera and which way it is pointing.
     * @param sceneSphere A way to approximate the overall bounds of the scene.
     * @param screenAspect The screen surface's width to height ratio.
     * @return The calculated transform matrix.
     */
    public static float[] calculateProjectionTransform(
            final ViewingAxis viewingAxis, final Sphere sceneSphere, float screenAspect) {
        // We start by calculating values for the NEAR and FAR clipping planes that snugly
        // embrace the nearest and furthest extents of the entire scene. These are our idealised
        // values.
        final float idealisedFar =
                calculateIdealisedFarPlane(viewingAxis, sceneSphere, screenAspect);
        final float idealisedNear =
                calculateIdealisedNearPlane(idealisedFar, sceneSphere.getRadius());

        // But the NEAR plane must exclude any part of the scene that is behind the camera,
        // which we can achieve by preventing it from going below zero. However using exactly zero
        // as the threshold is problematic. Perspective effect is derived from the ratio of
        // of the separation between the NEAR and FAR planes and the absolute value of NEAR.
        // This ratio tends to infinity as NEAR tends to zero. (The camera bumped into what it
        // is looking at).

        // So we will clamp NEAR above a threshold that is derived from a rule of thumb ratio.
        final float clampedNear = NEAR_PLANE_MINIMUM_FACTOR * idealisedFar;

        // We also wish to avoid the numerical problems from having the near and far planes too
        // close to each other, and push the far plane away arbitrarily to prevent this when
        // necessary.
        final float clampedFar = FAR_PLANE_MINIMUM_FACTOR * clampedNear;

        // Now we have the parameters we need to calculate the transform.
        return TransformFactory.perspective(
                FIELD_OF_VIEW_DEGREES, screenAspect, clampedNear, clampedFar);
    }

    /** Calculates a value for the FAR clipping plane, by positioning it snugly to match the
     * farthest extent of the scene contents - as implied by the scene's sphere.
     */
    private static float calculateIdealisedFarPlane(
            final ViewingAxis viewingAxis, final Sphere sceneSphere, float screenAspect) {
        // We want the far plane to skim the further bounds of the scene's sphere.
        // We can find a sample point on this plane by projecting the sphere's centre onto the
        // sphere's surface using a vector in the line-of-sight direction,
        final XYZf lineOfSightVector = viewingAxis.getLineOfSightVector();
        final float sphereRadius = sceneSphere.getRadius();
        final XYZf lineOfSightVectorScaledToSphereRadius =
                lineOfSightVector.vectorScaledToLength(sphereRadius);
        final XYZf pointWhereFarPlaneTouchesSphere =
                sceneSphere.getCentre().plus(lineOfSightVectorScaledToSphereRadius);
        // We can now build a vector that runs as the crow flies from the camera to this
        // far plane sampling point, and then dot-product it with the line of sight, to arrive
        // at our far plane distance relative to the camera.
        final XYZf asCrowFilesCamToSamplePoint =
                pointWhereFarPlaneTouchesSphere.minus(viewingAxis.getViewersPosition());
        float perpendicularDistanceFromCameraToFarPlane =
                asCrowFilesCamToSamplePoint.dotProduct(lineOfSightVector);
        return perpendicularDistanceFromCameraToFarPlane;
    }

    /** Calculates the NEAR clipping plane by trying to position it snugly at the nearest
     * extent of the scene contents, as implied by the scene's sphere.
     */
    private static float calculateIdealisedNearPlane(final float far, final float radius) {
        return far - 2 * radius;
    }
}
