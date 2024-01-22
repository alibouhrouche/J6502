package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Screen extends JPanel implements IOMemory {
    Canvas c;
    BufferedImage frameBuffer;
    Image image;
    WritableRaster r;
    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            Screen.this.image = Screen.this.frameBuffer.getScaledInstance(Screen.this.getWidth(), Screen.this.getHeight(), Image.SCALE_REPLICATE);
            Screen.this.repaint();
        }
    }
    final int[] RED = {0x000000, 0x240000, 0x490000, 0x6D0000, 0x920000, 0xB60000, 0xDB0000, 0xFF0000};
    final int[] GREEN = {0x000000, 0x002400, 0x004900, 0x006D00, 0x009200, 0x00B600, 0x00DB00, 0x00FF00};
    final int[] BLUE = {0x000000, 0x000055, 0x0000AA, 0x0000FF};
    Screen() {
        int[] colorMap = new int[256];
        for (int i = 0; i < colorMap.length; i++) {
            int red = (i >> 5) & 0b111;
            int green = (i >> 2) & 0b111;
            int blue = i & 0b11;
            colorMap[i] = RED[red] | GREEN[green] | BLUE[blue];
        }
        IndexColorModel colorModel = new IndexColorModel(8, colorMap.length, colorMap, 0, false, 3, DataBuffer.TYPE_BYTE);
        frameBuffer = new BufferedImage(128, 96, BufferedImage.TYPE_BYTE_INDEXED, colorModel);
        r = frameBuffer.getRaster();
        image = frameBuffer;
        addComponentListener(new ResizeListener());
    }

    public boolean inRange(int address) {
        return (address > 0x4000 && address < 0x8000);
    }

    public byte read(int address) {
        int x = address & 0x7F;
        int y = (address >> 7) & 0x7F;
        if (y > 96) return 0;
        byte[] out = new byte[8];
        r.getDataElements(x, y, out);
        return out[0];
    }

    public void write(int address, byte value) {
        int x = address & 0x7F;
        int y = (address >> 7) & 0x7F;
        if (y > 96) return;
        r.setDataElements(x, y, new byte[]{value});
        if (getWidth() == 0 || getHeight() == 0) {
            image = frameBuffer;
        } else {
            image = frameBuffer.getScaledInstance(getWidth(), getHeight(), Image.SCALE_REPLICATE);
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        Screen s = new Screen();
        f.add(s);
        f.setSize(128*5, 96*5);
        for (int y = 0; y < 96; y++) {
            for (int x = 0; x < 128; x++) {
                if (y == 48 && (x > 16 && x < 112)) s.write(0x4000 + x + (y << 7), (byte)0b00011100);
                if ((x-16) == y || (x-16) == (96 - y)) s.write(0x4000 + x + (y << 7), (byte)0b11100000);
                if (x == 64) s.write(0x4000 + x + (y << 7), (byte)0b00000010);
                if (((x-64)*(x-64) + (y-48)*(y-48) - 256) < 2) s.write(0x4000 + x + (y << 7), (byte)0b11100000);
            }
        }
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
