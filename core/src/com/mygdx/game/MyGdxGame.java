package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MyGdxGame extends ApplicationAdapter {
    public static final int SW = 1136;
    public static final int SH = 640;
    private String TAG = "MyGdxGame";
    private Stage stage;
    private Slider slider;
    private Image leftImg;
    private float sliderValue;
    private int scrollWidth;
    private int singleHeight = SH / 7;

    @Override
    public void create() {
        stage = new Stage(new FitViewport(SW, SH));
        Texture solobg = new Texture(Gdx.files.internal("solo/bg.jpg"));
        Texture lad = new Texture(Gdx.files.internal("solo/lab.png"));
        Texture pattern = new Texture(Gdx.files.internal("solo/pattern.png"));
        Texture right = new Texture(Gdx.files.internal("solo/right.png"));
        final Texture left = new Texture(Gdx.files.internal("solo/left.png"));
        Gdx.app.log(TAG, "screen width" + Gdx.graphics.getWidth());
        Gdx.app.log(TAG, "screen Height" + Gdx.graphics.getHeight());
        Image bgimg = new Image(solobg);
        leftImg = new Image(left);
        stage.addActor(bgimg);
        Table container = new Table();
        stage.addActor(container);
        container.setFillParent(true);
        container.add(leftImg).width(leftImg.getWidth());
        Gdx.input.setInputProcessor(stage);

        final Table table = new Table();
        final ScrollPane scroll = new ScrollPane(table);
        scroll.setFlickScroll(false);
        scroll.setScrollingDisabled(false, true);
        table.defaults().expand();
        for (int i = 12; i > 0; i--) {
            final Image image;
            switch (i) {
                case 12:
                case 9:
                case 7:
                case 5:
                case 3:
                    image = new Image(lad);
                    break;
                case 2:
                case 4:
                case 6:
                case 8:
                case 10:
                case 11:
                    image = new Image(pattern);
                    break;
                case 1:
                    image = new Image(right);
                    break;
                default:
                    image = new Image(pattern);
            }
            scrollWidth += image.getWidth();
            table.add(image);
        }
        Gdx.app.log(TAG, "TOTAL WIDTH;" + scrollWidth);
        scroll.addListener(new InputListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Gdx.app.log("scrollpane", "touch: " + x + "___" + y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        new Group().addActor(scroll);
        container.add(scroll).fill().expand();

        //slider  设置
        final Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        Texture background = new Texture(Gdx.files.internal("solo/slider_bg.png"));
        Texture knob = new Texture(Gdx.files.internal("solo/slider_knob.png"));
        sliderStyle.background = new TextureRegionDrawable(new TextureRegion(background));
        sliderStyle.knob = new TextureRegionDrawable(new TextureRegion(knob));
        int max = scrollWidth - SW + left.getWidth();
        slider = new Slider(0, max, 1, false, sliderStyle);
        slider.setWidth(background.getWidth());
        slider.setPosition(SW / 2 - background.getWidth() / 2, 0);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sliderValue = slider.getValue();
                scroll.setScrollX(slider.getValue());
            }
        });
//        slider.setValue(max);
        //添加slider
        stage.addActor(slider);

        container.addListener(new InputListener() {

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                judgePinAndXian(x, y, pointer);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        scrollWidth = 0;
    }

    private void judgePinAndXian(float x, float y, int pointer) {
        int chords = chords(y / singleHeight);
//        Gdx.app.log(TAG, "CHORDS:" + chords);
        float leftWidth = leftImg.getWidth();

        //1.判断 左侧区域 ：两种情况  ： 1） 该弦上没有手指按下 2） 有手指按下
        if (x <= leftWidth) {
            int pin = 0;
        } else {
            float v = x + sliderValue - leftWidth;
            int v1 = (int) Math.ceil(v / 130);
            Gdx.app.log(TAG, "v1:" + v1);
        }
    }

    private int chords(float v) {
        if (0 <= v && v < 1.5) {
            return 1;
        }
        if (1.5 < v && v < 2.5) {
            return 2;
        }
        if (2.5 < v && v < 3.5) {
            return 3;
        }
        if (3.5 < v && v < 4.5) {
            return 4;
        }
        if (4.5 < v && v < 5.5) {
            return 5;
        }
        if (5.5 < v && v < 6.5) {
            return 6;
        }
        if (v >= 6.5) {
            return 6;
        }
        return 1;
    }
}
