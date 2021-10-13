package com.jaeheonshim.towerheist.game.physics;

public class PlayerPhysicsConstants {
    public NumericValue maxVelocity = new NumericValue(10f);
    public NumericValue impulseX = new NumericValue(3f);
    public NumericValue impulseY = new NumericValue(10f);
    public NumericValue gOffset = new NumericValue(20);
    public NumericValue wallSlideVelocity = new NumericValue(0);
    public NumericValue decel = new NumericValue(200);
    public NumericValue wallBounce = new NumericValue(12);
    public NumericValue wallJumpY = new NumericValue(16);

    public static class NumericValue {
        public float value;

        public NumericValue(float value) {
            this.value = value;
        }
    }
}
