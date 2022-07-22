package org.spray.heaven.util;

import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.events.StrafeEvent;
import org.spray.heaven.main.Wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {

	public static float[] getMatrixRotations(Entity entityIn) {
		double diffX = entityIn.posX - Wrapper.getPlayer().posX;
		double diffZ = entityIn.posZ - Wrapper.getPlayer().posZ;
		double diffY;

		if (entityIn instanceof EntityLivingBase) {
			diffY = entityIn.posY + entityIn.getEyeHeight()
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight()) - 0.2f;
		} else {
			diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		}
		if (!Wrapper.getPlayer().canEntityBeSeen(entityIn)) {
			diffY = entityIn.posY + entityIn.height - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		}
		final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90)
				+ getFixedRotation(MathUtil.random(-1.75f, 1.75f)));
		float pitch = (float) ((Math.toDegrees(-Math.atan2(diffY, diffXZ)))
				+ getFixedRotation(MathUtil.random(-1.8f, 1.75f)));

		yaw = (Wrapper.getPlayer().rotationYaw
				+ getFixedRotation(MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw)));
		pitch = Wrapper.getPlayer().rotationPitch
				+ getFixedRotation(MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch));
		pitch = MathHelper.clamp(pitch, -90F, 90F);

		return new float[] { yaw, pitch };
	}

	public static float[] getRotations(final Entity entity) {
		float deltaX = (float) (entity.posX - Wrapper.getPlayer().posX);
		float deltaY = (float) ((entity.posY) - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight()));
		float deltaZ = (float) (entity.posZ - Wrapper.getPlayer().posZ);
		float distance = (float) Math.hypot(deltaX, deltaZ);
		float yaw = (float) ((float) (Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 85)
				- Math.cos(System.nanoTime() / 100000000) * 1);
		float pitch = (float) ((float) (Math.toDegrees(-Math.atan2(deltaY, distance)) - 10)
				+ Math.sin(System.nanoTime() / 100000000) * 1);
		float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.3F;
		float gcd = f * f * f * 1.3F;
		yaw -= yaw % gcd;
		pitch -= pitch % gcd;
		return new float[] { yaw, pitch };
	}

	public static float[] getMatrixRotations(Vec3d vec) {
		double diffX = vec.xCoord + 0.5 - Wrapper.getPlayer().posX;
		double diffY = vec.yCoord + 0.5 - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Wrapper.getPlayer().posZ;
		final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90)
				+ getFixedRotation(MathUtil.random(-1.75f, 1.75f)));
		float pitch = (float) ((Math.toDegrees(-Math.atan2(diffY, diffXZ)))
				+ getFixedRotation(MathUtil.random(-1.8f, 1.75f)));

		yaw = (Wrapper.getPlayer().rotationYaw
				+ getFixedRotation(MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw)));
		pitch = Wrapper.getPlayer().rotationPitch
				+ getFixedRotation(MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch));
		pitch = MathHelper.clamp(pitch, -90F, 90F);

		return new float[] { yaw, pitch };
	}

	public static float sunriseRotate(float from, float to, float minstep, float maxstep) {

		float f = MathHelper.wrapDegrees(to - from) * MathHelper.clamp(0.6f, 0, 1);

		if (f < 0) {
			f = MathHelper.clamp(f, -maxstep, -minstep);
		} else {
			f = MathHelper.clamp(f, minstep, maxstep);
		}
		if (Math.abs(f) > Math.abs(MathHelper.wrapDegrees(to - from)))
			return to;

		return from + f;
	}

	public static float[] getRotationsRandom(MotionEvent event, Entity entityIn) {
		double diffX = entityIn.posX - event.getPosX();
		double diffZ = entityIn.posZ - event.getPosZ();
		double diffY = entityIn.posY + (entityIn.getEyeHeight() * .8)
				- (event.getPosY() + Wrapper.getPlayer().getEyeHeight());

		if (entityIn instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP entityOtherPlayerMP = (EntityOtherPlayerMP) entityIn;
			diffY = diffY - (!(entityOtherPlayerMP).canEntityBeSeen(Wrapper.getPlayer()) ? -.45 : 0);
		}

		double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) ((Math.toDegrees(MathHelper.atan2(diffZ, diffX)) - 90)
				+ Math.abs(MathUtil.random(-1.5, 1.5)));
		float pitch = (float) ((-Math.toDegrees(MathHelper.atan2(diffY, diffXZ)))
				+ Math.abs(MathUtil.random(-1.5, 1.5)));
		yaw = (event.getYaw() + getFixedRotation(MathHelper.wrapDegrees(yaw - event.getYaw())));
		pitch = (event.getPitch() + getFixedRotation(MathHelper.wrapDegrees(pitch - event.getPitch())));
		pitch = MathHelper.clamp(pitch, -90, 90);
		return new float[] { yaw, pitch };
	}

	public static float[] lookAtEntity(Entity entityIn) {
		double d0 = entityIn.posX - Wrapper.getPlayer().posX;
		double d1 = entityIn.posZ - Wrapper.getPlayer().posZ;
		double d2;

		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
			d2 = entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() * 0.8
					- (Wrapper.getPlayer().posY + (double) Wrapper.getPlayer().getEyeHeight());
		} else {
			d2 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D
					- (Wrapper.getPlayer().posY + (double) Wrapper.getPlayer().getEyeHeight());
		}

		double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
		float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(d1, d0)) - 90.0f);
		float pitch = MathHelper.wrapDegrees((float) (-Math.toDegrees(Math.atan2(d2, d3))) + 5);
		yaw = Wrapper.getPlayer().rotationYaw
				+ getWellmoreFixed(MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw));
		pitch = Wrapper.getPlayer().rotationPitch
				+ getWellmoreFixed(MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch));

		return new float[] { yaw, pitch };
	}
	
	public static float[] lookDAtEntity(Entity entityIn) {
		double d0 = entityIn.posX - Wrapper.getPlayer().posX;
		double d1 = entityIn.posZ - Wrapper.getPlayer().posZ;
		double d2;

		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
			d2 = entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() * 0.8
					- (Wrapper.getPlayer().posY + (double) Wrapper.getPlayer().getEyeHeight());
		} else {
			d2 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D
					- (Wrapper.getPlayer().posY + (double) Wrapper.getPlayer().getEyeHeight());
		}

		double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
		float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(d1, d0)) - 90.0f);
		float pitch = MathHelper.wrapDegrees((float) (-Math.toDegrees(Math.atan2(d2, d3))) + 5);
		yaw = MathHelper.wrapDegrees(yaw);
		pitch = MathHelper.wrapDegrees(pitch);

		return new float[] { yaw, pitch };
	}

	public static float[] terrarRatation(final Entity e) {
		Vec3d eyesPos = new Vec3d(Minecraft.getMinecraft().player.posX + Math.random() / 10.0,
				Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight(),
				Minecraft.getMinecraft().player.posZ + Math.random() / 10.0);
		double diffX = e.getPositionVector().xCoord - eyesPos.xCoord;
		double diffY = e.getPositionVector().yCoord - eyesPos.yCoord;
		double diffZ = e.getPositionVector().zCoord - eyesPos.zCoord;
		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f);
		float pitch = MathHelper.wrapDegrees((float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) - 10.0f);
		float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
		float gcd = f * f * f * 1.2f;
		yaw -= yaw % gcd;
		pitch -= pitch % gcd;
		return new float[] { yaw, pitch };
	}

	public static float[] lookAtWellmore(MotionEvent event, Entity entityIn) {
		double diffX = entityIn.posX - Wrapper.getPlayer().posX;
		double diffZ = entityIn.posZ - Wrapper.getPlayer().posZ;
		double diffY;

		if (entityIn instanceof EntityLivingBase) {
			diffY = entityIn.posY + entityIn.getEyeHeight()
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight()) - 0.4;
		} else {
			diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		}

		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) ((float) (((Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f)) + MathUtil.random(-9, 9));
		float pitch = (float) ((float) (-(Math.atan2(diffY, dist) * 180.0 / Math.PI)) + MathUtil.random(-9, 9));
		yaw = event.getYaw() + getWellmoreFixed(MathHelper.wrapDegrees(yaw - event.getYaw()));
		pitch = event.getPitch() + getWellmoreFixed(MathHelper.wrapDegrees(pitch - event.getPitch()));

		return new float[] { yaw, pitch };
	}

	public static float[] lookAtRandomed(MotionEvent event, Entity entityIn) {
		Vec3d eyesPos = new Vec3d(event.getPosX(),
				Wrapper.getPlayer().getEntityBoundingBox().minY + Wrapper.getPlayer().getEyeHeight(), event.getPosZ());
		double diffX = entityIn.posX - event.getPosX();
		double diffZ = entityIn.posZ - event.getPosZ();
		double diffY;

		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
			diffY = entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() * 0.8
					- (event.getPosY() + (double) Wrapper.getPlayer().getEyeHeight());
		} else {
			diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D
					- (event.getPosY() + (double) Wrapper.getPlayer().getEyeHeight());
		}
		double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		double random = MathUtil.random(-1.5f, 1.5f);
		float yaw = (float) ((float) (Math.toDegrees(MathHelper.atan2(diffZ, diffX)) - 90) + random);
		float pitch = (float) ((-Math.toDegrees(MathHelper.atan2(diffY, diffXZ))) + random);
		yaw = (event.getYaw() + getFixedRotation(MathHelper.wrapDegrees(yaw - event.getYaw())));
		pitch = (event.getPitch() + getFixedRotation(MathHelper.wrapDegrees(pitch - event.getPitch())));
		pitch = MathHelper.clamp(pitch, -90, 90);
		return new float[] { yaw, pitch };
	}

	public static float[] lookAtBoundingBox(MotionEvent event, Entity entity) {
		AxisAlignedBB bb = entity.getEntityBoundingBox();

		float[] rotation = null;

		float yaw = 0;
		float pitch = 0;

		for (double xSearch = 0.36D; xSearch < 0.64D; xSearch += 0.1D) {
			for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
				for (double zSearch = 0.36D; zSearch < 0.64D; zSearch += 0.1D) {
					final Vec3d vec3 = new Vec3d(bb.minX + (bb.maxX - bb.minX) * xSearch,
							bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
					float[] vecRotation = predictRotation(vec3);

					if (rotation == null || getRotationDifference(vecRotation[0], vecRotation[1], event.getYaw(),
							event.getPitch()) < getRotationDifference(rotation[0], rotation[1], event.getYaw(),
									event.getPitch())) {
						rotation = vecRotation;

						// yaw = (float) (rotation[0] + MathUtil.random(-1.5, 1.5));
						// pitch = (float) (rotation[1] + MathUtil.random(-1.5, 1.5));
						yaw = rotation[0];
						pitch = rotation[1];

					}
				}
			}
		}

		return new float[] { yaw, pitch };
	}

	public static float[] predictRotation(final Vec3d vec) {
		final Vec3d eyesPos = new Vec3d(Wrapper.getPlayer().posX,
				Wrapper.getPlayer().getEntityBoundingBox().minY + Wrapper.getPlayer().getEyeHeight(),
				Wrapper.getPlayer().posZ);

		if (Wrapper.getPlayer().onGround)
			eyesPos.addVector(Wrapper.getPlayer().motionX, 0.0, Wrapper.getPlayer().motionZ);
		else
			eyesPos.addVector(Wrapper.getPlayer().motionX, Wrapper.getPlayer().motionY, Wrapper.getPlayer().motionZ);

		final double diffX = vec.xCoord - eyesPos.xCoord;
		final double diffY = vec.yCoord - eyesPos.yCoord;
		final double diffZ = vec.zCoord - eyesPos.zCoord;

		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) ((float) (((Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f)));
		float pitch = (float) ((float) (-(Math.atan2(diffY, dist) * 180.0 / Math.PI)));
		return new float[] { yaw, pitch };
	}

	public static float[] getLookNeeded(Entity entity) {
		return getLookNeeded(entity.posX, entity.posY, entity.posZ);
	}

	public static float[] getLookNeeded(double x, double y, double z) {
		return getLookNeeded(Wrapper.getPlayer(), x, y, z);
	}

	public static float[] getLookNeeded(Entity entity, double x, double y, double z) {
		double d = x + 0.5 - entity.posX;
		double g = y - entity.posY;
		double e = z + 0.5 - entity.posZ;

		double h = (double) Math.sqrt(d * d + e * e);
		float i = (float) (Math.atan2(e, d) * 180.0D / Math.PI) - 90.0F;
		float j = (float) (-(Math.atan2(g, h) * 180.0D / Math.PI));
		return new float[] { i, j };
	}

	private static float getAngle360(float dir, float yaw) {
		float f = Math.abs(yaw - dir) % 360.0f;
		float dist = f > 180.0f ? 360.0f - f : f;
		return dist;
	}

	public static boolean isInFOV(Entity player, Entity entity, double angle) {
		double angleDiff = getAngle360(player.rotationYaw,
				getLookNeeded(player, entity.posX, entity.posY, entity.posZ)[0]);
		return angleDiff > 0.0 && angleDiff < (angle *= 0.5) || -angle < angleDiff && angleDiff < 0.0;
	}

	public static boolean isInFOV(Entity entity, double angle) {
		return isInFOV(Wrapper.getPlayer(), entity, angle);
	}

	public static float getYaw(Entity entity) {
		double x = entity.posX - Wrapper.getPlayer().posX;
		double z = entity.posZ - Wrapper.getPlayer().posZ;
		float yaw = (float) (Math.atan2(x, z) * 57.29577951308232);
		return yaw;
	}

	public static float getAngleDifference(float a, float b) {
		return ((((a - b) % 360F) + 540F) % 360F) - 180F;
	}

	public static double normalizeAngle(double angle) {
		angle %= 360.0;

		if (angle >= 180.0) {
			angle -= 360.0;
		}

		if (angle < -180.0) {
			angle += 360.0;
		}

		return angle;
	}

	public static float limitAngleChange(float currentRotation, float targetRotation, float turnSpeed) {
		float diff = RotationUtil.getAngleDifference(targetRotation, currentRotation);

		return currentRotation + (diff > turnSpeed ? turnSpeed : Math.max(diff, -turnSpeed));
	}

	public static double getRotationDifference(float yawA, float pitchA, float yawB, float pitchB) {
		return Math.hypot(getAngleDifference(yawA, yawB), pitchA - pitchB);
	}

	public static float getGCD() {
		float f = (float) (0.2112676 * 0.6D + 0.2D);
		float gcd = (f * f * f * 8.0f) * 0.15f;
		return gcd;
	}

	public static float getWellmoreFixed(float value) {
		float gcd = MathHelper.wrapDegrees(getGCD());
		return Math.round((value - (value % gcd)) / getGCD()) * getGCD();
	}

	public static float getFixedRotation(float value) {
		// using setting sensitivity, but matrix integrated sensitive time checker (to
		// machine learning)
		float f1 = (f1 = (float) (Wrapper.MC.gameSettings.mouseSensitivity * .6 + .2)) * f1 * f1 * 8;
		return Math.round(value / (float) (f1 * .15)) * (float) (f1 * .15);
	}

	public static Vec3d getEyesPos() {
		return new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight(),
				Wrapper.getPlayer().posZ);
	}

	/**
	 * Returns "true" if the rotation falls into the entity at "targetEntity"
	 *
	 * @param targetEntity
	 * @param yaw
	 * @param pitch
	 * @param blockReachDistance
	 * @returnw
	 */
	public static boolean isFaced(Entity targetEntity, float yaw, float pitch, double blockReachDistance) {
		return EntityUtil.raycastEntity(blockReachDistance, yaw, pitch, entity -> entity == targetEntity) != null;
	}

	public static void strafeRotation(StrafeEvent event, float yaw) {
		int dif = (int) ((MathHelper.wrapDegrees(Wrapper.getPlayer().rotationYaw - yaw - 23.5f - 135) + 180) / 45);
		float strafe = event.getStrafe();
		float forward = event.getForward();
		float friction = event.getFriction();
		float calcForward = 0.0F;
		float calcStrafe = 0.0F;
		switch (dif) {
		case 0:
			calcForward = forward;
			calcStrafe = strafe;
			break;
		case 1:
			calcForward += forward;
			calcStrafe -= forward;
			calcForward += strafe;
			calcStrafe += strafe;
			break;
		case 2:
			calcForward = strafe;
			calcStrafe = -forward;
			break;
		case 3:
			calcForward -= forward;
			calcStrafe -= forward;
			calcForward += strafe;
			calcStrafe -= strafe;
			break;
		case 4:
			calcForward = -forward;
			calcStrafe = -strafe;
			break;
		case 5:
			calcForward -= forward;
			calcStrafe += forward;
			calcForward -= strafe;
			calcStrafe -= strafe;
			break;
		case 6:
			calcForward = -strafe;
			calcStrafe = forward;
			break;
		case 7:
			calcForward += forward;
			calcStrafe += forward;
			calcForward -= strafe;
			calcStrafe += strafe;
		}

		if (calcForward > 1.0F || calcForward < 0.9F && calcForward > 0.3F || calcForward < -1.0F
				|| calcForward > -0.9F && calcForward < -0.3F) {
			calcForward *= 0.5F;
		}

		if (calcStrafe > 1.0F || calcStrafe < 0.9F && calcStrafe > 0.3F || calcStrafe < -1.0F
				|| calcStrafe > -0.9F && calcStrafe < -0.3F) {
			calcStrafe *= 0.5F;
		}

		float d = calcStrafe * calcStrafe + calcForward * calcForward;
		if (d >= 1.0E-4F) {
			d = MathHelper.sqrt(d);
			if (d < 1.0F) {
				d = 1.0F;
			}

			d = friction / d;
			calcStrafe *= d;
			calcForward *= d;
			float yawSin = MathHelper.sin((float) ((double) yaw * 3.141592653589793D / (double) 180.0F));
			float yawCos = MathHelper.cos((float) ((double) yaw * 3.141592653589793D / (double) 180.0F));
			Wrapper.getPlayer().motionX += (double) (calcStrafe * yawCos) - (double) calcForward * (double) yawSin;
			Wrapper.getPlayer().motionZ += (double) (calcForward * yawCos) + (double) calcStrafe * (double) yawSin;
		}
	}

}
