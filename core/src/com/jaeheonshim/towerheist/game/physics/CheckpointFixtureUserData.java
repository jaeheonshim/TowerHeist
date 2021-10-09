package com.jaeheonshim.towerheist.game.physics;

import com.jaeheonshim.towerheist.game.objects.Checkpoint;

public class CheckpointFixtureUserData extends FixtureUserData {
    private final Checkpoint checkpoint;

    public CheckpointFixtureUserData(Checkpoint checkpoint) {
        super(FixtureType.CHECKPOINT);

        this.checkpoint = checkpoint;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }
}
