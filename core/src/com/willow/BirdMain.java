package com.willow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class BirdMain extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img,bird,lost,start;
	float bw,bh,sw,sh, bx, by;
	float velocity = 0.0f;
	float gravity = 0.6f;
	int state =0;
	Texture bee1,bee2,bee3;
	int nbees =3;
	float beesx[]= new float[nbees];
	float beesy[][]= new float[3][nbees];
//	ShapeRenderer sr;
	Circle c_bird,c_bee1[],c_bee2[],c_bee3[];
	int score;
	Boolean flag = true;
	BitmapFont font1,font2;
	Sound sound1,sound2,sound3;
	Boolean sound1Flag=true;
	Boolean sound2Flag=true;
	Boolean sound3Flag=true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("voodoo_cactus_island.png");
		bird = new Texture("frame-1.png");
		bee1 = new Texture("1.png");
		bee2 = new Texture("2.png");
		bee3 = new Texture("1.png");
		start = new Texture("start.png");
		lost = new Texture("lost.png");
		bw=Gdx.graphics.getWidth()/13;
		bh=Gdx.graphics.getHeight()/11;
		sw=Gdx.graphics.getWidth();
		sh=Gdx.graphics.getHeight();
		bx =Gdx.graphics.getWidth()/4;
		by =Gdx.graphics.getHeight();

		font1 = new BitmapFont();
		font1.setColor(Color.BROWN);
		font1.getData().setScale(6);

		font2 = new BitmapFont();
		font2.setColor(Color.BROWN);
		font2.getData().setScale(8);


		c_bird =new Circle();
		c_bee1 = new Circle[nbees];
		c_bee2 = new Circle[nbees];
		c_bee3 = new Circle[nbees];

		sound1=Gdx.audio.newSound(Gdx.files.internal("pig3.mp3"));
		sound2=Gdx.audio.newSound(Gdx.files.internal("kuce.mp3"));
		sound3=Gdx.audio.newSound(Gdx.files.internal("tapSound.mp3"));
//		sr = new ShapeRenderer();

		for (int i=0; i<nbees; i++){
			flag=true;
			beesx[i] = sw + i *sw/2;
			Random r1 =new Random();
			Random r2 =new Random();
			Random r3 =new Random();

			beesy[0][i]= r1.nextFloat()*sh-(bh/2);
			beesy[1][i]= r2.nextFloat()*sh-(bh/2);
			beesy[2][i]= r3.nextFloat()*sh-(bh/2);

			c_bee1[i]=new Circle();
			c_bee2[i]=new Circle();
			c_bee3[i]=new Circle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(img, 0, 0, sw, sh);
		batch.draw(bird, bx, by, bw, bh);
/**
* State 1
* */
		if (state == 1) {
			sound1Flag=true;
			sound2Flag=false;

			if (Gdx.input.justTouched()) {
				velocity = -20;
				sound3Flag=true;
				if(sound3Flag){
					sound3.play();
					sound3Flag=false;
				}
			}
			for (int i=0;i<nbees;i++){
				if(beesx[i]<0){
					flag=true;
					beesx[i] = nbees *sw/2;
					Random r1 =new Random();
					Random r2 =new Random();
					Random r3 =new Random();

					beesy[0][i]= r1.nextFloat()*sh-(bh/2);
					beesy[1][i]= r2.nextFloat()*sh-(bh/2);
					beesy[2][i]= r3.nextFloat()*sh-(bh/2);
				}
				font1.draw(batch,"SCORE: "+String.valueOf(score),bh,bh);
				if (bx>beesx[i] && flag){
					score++;
					flag=false;
				}

				beesx[i] -= 15;
				batch.draw(bee1,beesx[i],beesy[0][i],bw,bh);
				batch.draw(bee2,beesx[i],beesy[1][i],bw,bh);
				batch.draw(bee3,beesx[i],beesy[2][i],bw,bh);
			}
			if (by < 0-(2*bh) || by>sh) {
				state=2;
			}
			else {
				velocity = velocity + gravity;
				by = by - velocity;
			}
		}
/**
 * STATE 2
 * */
		else if (state==2) {
			by = sw / 3;
			velocity = 0;
			font1.draw(batch,"SCORE: "+String.valueOf(score),bh,bh);
			batch.draw(lost, 0, 0, sw, sh);
			if (sound1Flag) {
				sound1.play();
				sound1Flag=false;
			}
			if (Gdx.input.justTouched()) {
				bx = Gdx.graphics.getWidth() / 4;
				by = Gdx.graphics.getHeight();
				score=0;

				for (int i = 0; i < nbees; i++) {
					beesx[i] = sw + i * sw / 2;
					Random r1 = new Random();
					Random r2 = new Random();
					Random r3 = new Random();

					beesy[0][i] = r1.nextFloat() * sh;
					beesy[1][i] = r2.nextFloat() * sh;
					beesy[2][i] = r3.nextFloat() * sh;

					c_bee1[i] = new Circle();
					c_bee2[i] = new Circle();
					c_bee3[i] = new Circle();
				}
				state = 1;
			}
		}
/**
 * STATE 0
 * */
		else if (state==0) {
			batch.draw(start, 0, 0, sw, sh);
			if (Gdx.input.justTouched()) {
				if (sound2Flag) {
					sound2.play();
				}
			state=1;
			}
		}

		//		sr.begin(ShapeRenderer.ShapeType.Filled);
		c_bird.set(bx+bw/2,by+bh/2,bw/4);
		for(int i=0;i<nbees;i++){
			c_bee1[i].set(beesx[i]+bw/2,beesy[0][i]+bh/2,bw/4);
			c_bee2[i].set(beesx[i]+bw/2,beesy[1][i]+bh/2,bw/4);
			c_bee3[i].set(beesx[i]+bw/2,beesy[2][i]+bh/2,bw/4);
//			sr.setColor(Color.BLUE);
//			sr.circle(beesx[i]+bw/2,beesy[0][i]+bh/2,bw/4);
//			sr.circle(beesx[i]+bw/2,beesy[1][i]+bh/2,bw/4);
//			sr.circle(beesx[i]+bw/2,beesy[2][i]+bh/2,bw/4);
//			sr.circle(bx+bw/2,by+bh/2,bw/4);
			if(Intersector.overlaps(c_bird,c_bee1[i]) || Intersector.overlaps(c_bird,c_bee2[i]) ||
					Intersector.overlaps(c_bird,c_bee3[i])){
				state=2;
			}
		}
//		sr.end();
		batch.end();
	}
	@Override
	public void dispose () {

	}
}
