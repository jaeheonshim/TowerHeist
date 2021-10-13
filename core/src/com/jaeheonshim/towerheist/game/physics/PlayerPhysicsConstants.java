package com.jaeheonshim.towerheist.game.physics;

public class PlayerPhysicsConstants {
    public NumericValue maxVelocity = new NumericValue(10.5f);
    public NumericValue impulseX = new NumericValue(2f);
    public NumericValue impulseY = new NumericValue(11f);
    public NumericValue gOffset = new NumericValue(17);
    public NumericValue wallSlideVelocity = new NumericValue(2.5f);
    public NumericValue decel = new NumericValue(90);
    public NumericValue wallBounce = new NumericValue(15);
    public NumericValue wallJumpY = new NumericValue(13);

    public static class NumericValue {
        public float value;

        public NumericValue(float value) {
            this.value = value;
        }
    }
}
