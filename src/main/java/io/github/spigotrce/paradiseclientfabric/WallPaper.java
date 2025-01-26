package io.github.spigotrce.paradiseclientfabric;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Random;

public class WallPaper {
    private static final Random random = new Random();
    private static final int[] drops = new int[300];
    private static final Particle[] particles = new Particle[100];

    static {
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }
    }

    public static String getTheme() {
        return ConfigManager.getTheme();
    }

    public static void setTheme(String theme) {
        ConfigManager.setTheme(theme);
    }

    /**
     * Renders the background according to the current theme.
     * Switches between themes and calls the appropriate render method.
     */
    public static void render(DrawContext context, int width, int height) {
        String theme = getTheme(); // Retrieve the theme from configuration
        switch (theme) {
            case "ParadiseHack" -> renderMatrix(context, width, height);
            case "ParadiseParticle" -> renderElegantBackground(context, width, height);
            default -> renderMatrix(context, width, height); // Default to "Hack" theme
        }
    }

    /**
     * Renders the "Matrix" style theme with falling characters.
     */
    public static void renderMatrix(DrawContext context, int width, int height) {
        context.fillGradient(0, 0, width, height, 0xCC000000, 0xCC000000); // Black gradient background
        for (int i = 0; i < drops.length; i++) {
            String text = Helper.generateRandomString(1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", random);
            context.drawText(MinecraftClient.getInstance().textRenderer, text, i * 10, drops[i] * 10, 0x00FF00, false);

            if (drops[i] * 10 > height && random.nextDouble() > 0.975)
                drops[i] = 0;
            drops[i]++;
        }
    }

    /**
     * Renders the particle-based elegant background theme.
     */
    private static int lastWidth = -1;
    private static int lastHeight = -1;

    public static void renderElegantBackground(DrawContext context, int width, int height) {
        // Check if the window size has changed
        if (width != lastWidth || height != lastHeight) {
            regenerateParticles(width, height);
            lastWidth = width;
            lastHeight = height;
        }

        // Draw the gradient background
        context.fillGradient(0, 0, width, height, 0x801A237E, 0x80882dbd);

        // Update and draw each particle
        for (Particle particle : particles) {
            particle.update(width, height);
            context.fill(particle.x, particle.y, particle.x + 2, particle.y + 2, 0x80FFFFFF);
        }
    }

    /**
     * Regenerates all particles to adapt to the new window size.
     */
    private static void regenerateParticles(int width, int height) {
        for (Particle particle : particles) {
            particle.reset(width, height);
        }
    }

    /**
     * Represents an individual particle with position, velocity, and color.
     */
    private static class Particle {
        int x, y, speedX, speedY, color;

        public Particle() {
            this(0, 0); // Initialize without specific dimensions
        }

        public Particle(int width, int height) {
            reset(width, height);
        }

        /**
         * Updates the particle's position and resets it if it moves out of bounds.
         */
        public void update(int width, int height) {
            x += speedX;
            y += speedY;

            if (x < 0 || x > width || y < 0 || y > height) {
                reset(width, height);
            }
        }

        /**
         * Resets the particle to a new random position, speed, and color.
         */
        public void reset(int width, int height) {
            x = random.nextInt(Math.max(width, 1));
            y = random.nextInt(Math.max(height, 1));

            do {
                speedX = -1 + random.nextInt(3); // Velocity between -1 and 1
                speedY = -1 + random.nextInt(3);
            } while (speedX == 0 && speedY == 0);

            color = 0xFFFFFFFF; // White color
        }
    }
}
