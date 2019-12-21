package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class NPC
{
    public static BufferedImage im;
    public static Random random = new Random();
    public static ArrayList<NPC> list = new ArrayList<>();
    public static ArrayList<NPC> temp_list = new ArrayList<>();
    static int id_counter = 1;
    boolean alive;
    public int id;
    double speed;
    int hp;
    int base_hp;
    int damage;
    double x;
    double y;
    long life;
    long creation_time;
    long time_between_fire;
    long time_left_until_fire = time_between_fire;
    double dx;
    double dy;
    int start_x;
    int start_y;
    int dir_x;
    int dir_y;
    int width;
    int height;
    Color color;

    public NPC(Color color, double speed, int hp, int damage, long life, long time_between_fire, int dir_x, int dir_y, int width, int height)
    {
        creation_time = System.nanoTime();
        id = id_counter;
        id_counter++;
        this.alive = true;
        this.time_between_fire = time_between_fire;
        this.color = color;
        this.speed = speed;
        this.hp = hp;
        this.base_hp = hp;
        this.damage = damage;
        this.life = life;
        this.dir_x = dir_x;
        this.dir_y = dir_y;
        this.width = width;
        this.height = height;

        int dir = random.nextInt(4);
        switch (dir)
        {
            case 0:
                start_x = random.nextInt(Game.width);
                x = start_x;
                this.dir_x = random.nextInt(Game.width);
                start_y  = 0;
                y = start_y;
                this.dir_y = Game.height;
                break;
            case 1:
                start_x = Game.width;
                x = start_x;
                this.dir_x = 0;
                start_y = random.nextInt(Game.height);
                y = start_y;
                this.dir_y = random.nextInt(Game.height);
                break;
            case 2:
                start_x = random.nextInt(Game.width);
                x = start_x;
                this.dir_x = random.nextInt(Game.width);
                start_y = Game.height;
                y = start_y;
                this.dir_y = 0;
                break;
            case 3:
                start_x = 0;
                x = start_x;
                this.dir_x = Game.width;
                start_y = random.nextInt(Game.height);
                y = start_y;
                this.dir_y =  random.nextInt(Game.height);
                break;
        }

        calculateDxDy();
    }

    public void calculateDxDy()
    {
        int temp_dx = dir_x - start_x;
        int temp_dy = dir_y - start_y;
        double k = speed / Math.sqrt(temp_dx * temp_dx + temp_dy * temp_dy);
        dx = k * temp_dx;
        dy = k * temp_dy;
    }

    public void update()
    {
        time_left_until_fire--;
        if (System.nanoTime() - creation_time < life)
        {
            if(hp > 0)
            {
                if(((int) x > -1)&&((int) x < Game.width + 1)&&((int) y > -1)&&((int) y < Game.height + 1))
                {
                    x = x + dx;
                    y = y + dy;
                    if(time_left_until_fire < 0)
                    {
                        shoot();
                        time_left_until_fire = time_between_fire;
                    }
                }
                else
                {
                    alive = false;
                }
            }
            else
            {
                alive = false;
            }
        }
        else
        {
            alive = false;
        }
    }

    public static void updateAllNPC()
    {
        for(NPC n : list)
        {
            if(n.alive)
            {
                n.update();
            }
        }
    }

    public void shoot()
    {
        Bullet.temp_list.add(new Bullet(id, new Color(128, 0, 128), speed * 2, damage, life, (int) x, (int) y, (int) (Game.x + random.nextInt( 2 * Game.p_width) - Game.p_width), (int) (Game.y + random.nextInt( 2 * Game.p_height) - Game.p_height), 12, 12 ));
    }

    public static void generateNPC(Color color, double speed, int hp, int damage, long life, long time_between_fire, int dir_x, int dir_y, int width, int height)
    {
        NPC.temp_list.add(new NPC(color, speed, hp, damage, life, time_between_fire, dir_x, dir_y, width, height));
    }

    public void draw(Graphics g)
    {
        //g.setColor(color);
       // g.fillOval((int) x - width/2, (int) y - height/2, width, height);
        g.drawImage(NPC.im, (int) x - width/2, (int) y - height/2, null);
        Game.drawHP(g,base_hp, hp, (int) x, (int) y, height/2);
    }

    public static void draw_all(Graphics g)
    {
        list.addAll(temp_list);
        temp_list.clear();
        for(NPC n : list)
        {
            if(n.alive)
            {
                n.draw(g);
            }
        }
    }
}
