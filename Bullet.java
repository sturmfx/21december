package com.company;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bullet
{
    public static BufferedImage im;
    public static ArrayList<Bullet> list = new ArrayList<>();
    public static ArrayList<Bullet> temp_list = new ArrayList<>();
    static int id_counter = 0;
    boolean alive = true;
    int id;
    public int NPCid;
    double speed;
    int damage;
    double x;
    double y;
    long life;
    long creation_time;
    double dx;
    double dy;
    int start_x;
    int start_y;
    int dir_x;
    int dir_y;
    int width;
    int height;
    Color color;

    public Bullet(int NPCid, Color color, double speed, int damage, long life, int start_x, int start_y, int dir_x, int dir_y, int width, int height)
    {
        creation_time = System.nanoTime();
        id = id_counter;
        id_counter++;
        this.NPCid = NPCid;
        this.color = color;
        this.speed = speed;
        this.damage = damage;
        this.life = life;
        this.start_x = start_x;
        this.x = start_x;
        this.y = start_y;
        this.start_y = start_y;
        this.dir_x = dir_x;
        this.dir_y = dir_y;
        this.width = width;
        this.height = height;

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
        //
        if (System.nanoTime() - creation_time < life)
        {
            x = x + dx;
            y = y + dy;
        }
        else
        {
            alive = false;
        }
    }

    public static void updateAllBullet()
    {
        for(Bullet n : list)
        {
            if(n.alive)
            {
                n.collision_check();
                n.update();

            }
        }
    }

    public void collision_check()
    {
        double x_dif;
        double y_dif;
        double xy_dif;
        if(NPCid == 0)
        {
            for (NPC n : NPC.list)
            {
                if (n.alive && (NPCid != n.id))
                {
                    x_dif = Math.abs(x - n.x);
                    y_dif = Math.abs(y - n.y);
                    xy_dif = Math.sqrt(x_dif * x_dif + y_dif * y_dif);
                    if ((double) (width / 2 + n.width / 2) > xy_dif)
                    {
                        this.alive = false;
                        n.hp = n.hp - damage;
                        Game.shoots_hit++;
                        if (n.hp < 0) Game.enemy_destroyed++;
                    }
                }
            }
        }
        else
        {
            x_dif = Math.abs(x - Game.x);
            y_dif = Math.abs(y - Game.y);
            xy_dif = Math.sqrt(x_dif * x_dif + y_dif * y_dif);
            if ((double) (width / 2 + Game.p_width / 2) > xy_dif)
            {
                this.alive = false;
                Game.hp = Game.hp - damage;


            }
        }
    }

    public void draw(Graphics g)
    {
        //g.setColor(color);
        //g.fillOval((int) x - width/2, (int) y - height/2, width , height );
        g.drawImage(Bullet.im, (int) x - width/2, (int) y - height/2, null);
    }

    public static void draw_all(Graphics g)
    {
        list.addAll(temp_list);
        temp_list.clear();
        for(Bullet n : list)
        {
            if(n.alive)
            {
                n.draw(g);
            }
        }
    }

    public static void newPlayerBullet(MouseEvent e)
    {
        Bullet.temp_list.add(new Bullet(0, Game.bullet_color, Game.bullet_speed, Game.bullet_damage, Game.bullet_life, (int) Game.x, (int) Game.y, e.getX(), e.getY(), Game.bullet_width, Game.bullet_height ));
    }
}
