package com.m4thg33k.pipedream.core.util;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This code lovingly borrowed from EnderIO
 */
public class RaytraceResult {

    public final RayTraceResult movingObjectPosition;

    public static RaytraceResult getClosestHit(Vec3d origin, Collection<RaytraceResult> candidates)
    {
        double minLengthSquared = Double.POSITIVE_INFINITY;
        RaytraceResult closest = null;

        for (RaytraceResult candidate : candidates)
        {
            RayTraceResult hit = candidate.movingObjectPosition;

            if (hit != null)
            {
                double lengthSquared = hit.hitVec.squareDistanceTo(origin);

                if (lengthSquared < minLengthSquared)
                {
                    minLengthSquared = lengthSquared;
                    closest = candidate;
                }
            }
        }
        return closest;
    }

    public static void sort(final Vec3d origin, List<RaytraceResult> toSort)
    {
        if (origin==null || toSort == null)
        {
            return;
        }

        Collections.sort(toSort, new Comparator<RaytraceResult>() {
            @Override
            public int compare(RaytraceResult o1, RaytraceResult o2) {
                return Double.compare(o1.getDistanceTo(origin), o2.getDistanceTo(origin));
            }
        });
    }

    public RaytraceResult(RayTraceResult movingObjectPosition)
    {
        this.movingObjectPosition = movingObjectPosition;
    }

    public double getDistanceTo(Vec3d origin)
    {
        if (movingObjectPosition == null || origin==null)
        {
            return Double.MAX_VALUE;
        }

        return movingObjectPosition.hitVec.squareDistanceTo(origin);
    }
}
