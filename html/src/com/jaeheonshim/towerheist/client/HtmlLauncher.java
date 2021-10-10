package com.jaeheonshim.towerheist.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import com.jaeheonshim.towerheist.TowerHeist;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new TowerHeist();
        }

        @Override
        public Preloader.PreloaderCallback getPreloaderCallback() {
                return createPreloaderPanel(GWT.getHostPageBaseURL() + "loading.png");
        }

        @Override
        protected void adjustMeterPanel(Panel meterPanel, Style meterStyle) {
                meterPanel.setStyleName("gdx-meter");
                meterPanel.setStyleName("nostripes");
                meterStyle.setProperty("backgroundColor", "#5cff99");
        }
}