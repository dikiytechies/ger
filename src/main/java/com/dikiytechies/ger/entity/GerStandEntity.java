package com.dikiytechies.ger.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GerStandEntity extends StandEntity {

    private static final DataParameter<Float> BEAM_X = EntityDataManager.defineId(GerStandEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> BEAM_Y = EntityDataManager.defineId(GerStandEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> BEAM_Z = EntityDataManager.defineId(GerStandEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> BEAM_TICKS = EntityDataManager.defineId(GerStandEntity.class, DataSerializers.INT);

    public GerStandEntity(StandEntityType<GerStandEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BEAM_X, 0.0F);
        this.entityData.define(BEAM_Y, 0.0F);
        this.entityData.define(BEAM_Z, 0.0F);
        this.entityData.define(BEAM_TICKS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            int ticks = this.entityData.get(BEAM_TICKS);
            if (ticks > 0) {
                this.entityData.set(BEAM_TICKS, ticks - 1);
            }
        }
    }
    public void fireBeam(Vector3d endPos, int ticks) {
        this.entityData.set(BEAM_X, (float) endPos.x);
        this.entityData.set(BEAM_Y, (float) endPos.y);
        this.entityData.set(BEAM_Z, (float) endPos.z);
        this.entityData.set(BEAM_TICKS, ticks);
    }

    public int getBeamTicks() {
        return this.entityData.get(BEAM_TICKS);
    }

    public Vector3d getBeamEnd() {
        return new Vector3d(this.entityData.get(BEAM_X), this.entityData.get(BEAM_Y), this.entityData.get(BEAM_Z));
    }
}
