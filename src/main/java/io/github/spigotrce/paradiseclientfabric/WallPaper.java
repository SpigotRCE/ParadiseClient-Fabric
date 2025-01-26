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
     * Rend l'arrière-plan selon le thème actuel.
     */
    public static void render(DrawContext context, int width, int height) {
        String theme = getTheme(); // Récupérer le thème à partir de la configuration
        switch (theme) {
            case "ParadiseHack" -> renderMatrix(context, width, height);
            case "ParadiseParticle" -> renderElegantBackground(context, width, height);
            default -> renderMatrix(context, width, height); // Hack par défaut
        }
    }


    // Thème Hack (style Matrix)
    public static void renderMatrix(DrawContext context, int width, int height) {
        context.fillGradient(0, 0, width, height, 0xCC000000, 0xCC000000);
        for (int i = 0; i < drops.length; i++) {
            String text = Helper.generateRandomString(1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", random);
            context.drawText(MinecraftClient.getInstance().textRenderer, text, i * 10, drops[i] * 10, 0x00FF00, false);

            if (drops[i] * 10 > height && random.nextDouble() > 0.975)
                drops[i] = 0;
            drops[i]++;
        }
    }

    // Thème Particle (particules dynamiques)
    public static void renderElegantBackground(DrawContext context, int width, int height) {
        context.fillGradient(0, 0, width, height, 0xFF1A237E, 0xFF882dbd); // Bleu -> Violet
        for (Particle particle : particles) {
            particle.update(width, height);
            context.fill(particle.x, particle.y, particle.x + 2, particle.y + 2, particle.color);
        }
    }

    private static class Particle {
        int x, y, speedX, speedY, color;

        public Particle() {
            reset(800, 600);
        }

        public void update(int width, int height) {
            x += speedX;
            y += speedY;

            if (x < 0 || x > width || y < 0 || y > height) {
                reset(width, height);
            }
        }

        public void reset(int width, int height) {
            x = random.nextInt(width);
            y = random.nextInt(height);

            do {
                speedX = -1 + random.nextInt(3); // Vitesse entre -1 et 1
                speedY = -1 + random.nextInt(3);
            } while (speedX == 0 && speedY == 0);

            color = 0xFFFFFFFF; // Blanc
        }
    }
}
