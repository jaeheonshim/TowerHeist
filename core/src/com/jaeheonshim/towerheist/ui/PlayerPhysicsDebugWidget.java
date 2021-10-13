package com.jaeheonshim.towerheist.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jaeheonshim.towerheist.game.Player;
import com.jaeheonshim.towerheist.game.physics.PlayerPhysicsConstants;

public class PlayerPhysicsDebugWidget extends Window {
    private PlayerPhysicsConstants playerPhysicsConstants = Player.constants;

    private Skin skin;

    private SliderParameter maxVelocity;
    private SliderParameter impulseX;
    private SliderParameter impulseY;
    private SliderParameter gOffset;
    private SliderParameter wallSlideVelocity;
    private SliderParameter decel;
    private SliderParameter wallBounce;
    private SliderParameter wallJumpY;

    public PlayerPhysicsDebugWidget(Skin skin) {
        super("Player Physics", skin);
        this.skin = skin;

        maxVelocity = new SliderParameter("Max Velocity", playerPhysicsConstants.maxVelocity);
        impulseX = new SliderParameter("Impulse X", playerPhysicsConstants.impulseX);
        impulseY = new SliderParameter("Impulse Y", playerPhysicsConstants.impulseY);
        gOffset = new SliderParameter("G Offset", playerPhysicsConstants.gOffset);
        wallSlideVelocity = new SliderParameter("Wall Slide", playerPhysicsConstants.wallSlideVelocity);
        decel = new SliderParameter("Decel", playerPhysicsConstants.decel, 0, 210, 10);
        wallBounce = new SliderParameter("Wall Bounce", playerPhysicsConstants.wallBounce);
        wallJumpY = new SliderParameter("Wall Jump", playerPhysicsConstants.wallJumpY);

        addParameter(maxVelocity);
        addParameter(impulseX);
        addParameter(impulseY);
        addParameter(gOffset);
        addParameter(wallSlideVelocity);
        addParameter(decel);
        addParameter(wallBounce);
        addParameter(wallJumpY);
    }

    private void addParameter(SliderParameter parameter) {
        add(parameter.label).spaceRight(10);
        add(parameter.slider);
        row();
    }

    class SliderParameter {
        final String labelText;
        Label label;
        Slider slider;
        PlayerPhysicsConstants.NumericValue numericValue;

        SliderParameter(String labelText, PlayerPhysicsConstants.NumericValue numericValue) {
            this(labelText, numericValue, 0, 30, 0.5f);
        }

        SliderParameter(String labelText, PlayerPhysicsConstants.NumericValue numericValue, float min, float max, float step) {
            this.numericValue = numericValue;
            this.labelText = labelText + " (%3.1f)";
            this.label = new Label(String.format(this.labelText, numericValue.value), PlayerPhysicsDebugWidget.this.skin);
            slider = new Slider(min, max, step, false, PlayerPhysicsDebugWidget.this.skin);
            slider.setValue(numericValue.value);

            slider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    label.setText(String.format(SliderParameter.this.labelText, slider.getValue()));
                    numericValue.value = slider.getValue();
                }
            });
        }
    }
}
