package net.slimey.aquatical.entity.ai;

        import java.util.List;
        import java.util.stream.Stream;
        import javax.annotation.Nullable;
        import net.minecraft.nbt.CompoundTag;
        import net.minecraft.world.DifficultyInstance;
        import net.minecraft.world.entity.EntityType;
        import net.minecraft.world.entity.MobSpawnType;
        import net.minecraft.world.entity.SpawnGroupData;
        import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
        import net.minecraft.world.entity.animal.AbstractSchoolingFish;
        import net.minecraft.world.level.Level;
        import net.minecraft.world.level.ServerLevelAccessor;

public abstract class LargeShoal extends AbstractSchoolingFish {
    @Nullable
    private LargeShoal leader;
    private int schoolSize = 1;

    public LargeShoal(EntityType<? extends LargeShoal> p_27523_, Level p_27524_) {
        super(p_27523_, p_27524_);
    }

    protected void registerGoals() {super.registerGoals();
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */

    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }

    protected boolean canRandomSwim() {
        return !this.isFollower();
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    public LargeShoal startFollowing(LargeShoal p_27526_) {
        this.leader = p_27526_;
        p_27526_.addFollower();
        return p_27526_;
    }

    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }

    private void addFollower() {
        ++this.schoolSize;
    }

    private void removeFollower() {
        --this.schoolSize;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
    }

    /**
     * Called to update the entity's position/logic.
     */


    public void tick() {
        super.tick();
        if (this.hasFollowers() && this.level.random.nextInt(200) == 1) {
            List<? extends AbstractSchoolingFish> list = this.level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.schoolSize = 1;
            }
        }

    }


    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }

    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 121.0D;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.leader, 1.0D);
        }

    }

    @Override
    public void addFollowers(Stream<? extends AbstractSchoolingFish> p_27534_) {
        p_27534_.limit((long)(this.getMaxSchoolSize() - this.schoolSize)).filter((p_27538_) -> {
            return p_27538_ != this;
        }).forEach((p_27536_) -> {
            p_27536_.startFollowing(this);
        });
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pSpawnData == null) {
            pSpawnData = new LargeShoal.SchoolSpawnGroupData(this);
        } else {
            this.startFollowing(((LargeShoal.SchoolSpawnGroupData)pSpawnData).leader);
        }

        return pSpawnData;
    }

    public static class SchoolSpawnGroupData implements SpawnGroupData {
        public final LargeShoal leader;

        public SchoolSpawnGroupData(LargeShoal p_27553_) {
            this.leader = p_27553_;
        }
    }
}