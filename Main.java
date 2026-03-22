package main.java;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.BufferUtils;

/**
 * Shadow Mapping Workshop — Starter projekt
 *
 * Scéna: podlaha + kocky, Phong osvetlenie, BEZ tieňov.
 * Vaša úloha: implementovať shadow mapping pomocou TODO blokov.
 *
 * Cvičenie 1 — Depth pass  → hľadaj [CV1]
 * Cvičenie 2 — Shadow test → hľadaj [CV2]
 *
 * Shadery sú v priečinku /shaders (v koreni projektu).
 */
public class Main {



    // Okno
    static final int WIDTH  = 1280;
    static final int HEIGHT = 720;

    // kamera
    static float camYaw   = -90f;
    static float camPitch =  -20f;
    static float camDist  =  10f;
    static float lastX = WIDTH / 2f, lastY = HEIGHT / 2f;
    static boolean firstMouse = true;

    // Svetlo
    static final Vector3f LIGHT_POS = new Vector3f(3.0f, 5.0f, 3.0f);

    // Shadow map rozlíšenie
    static final int SHADOW_WIDTH  = 1024;
    static final int SHADOW_HEIGHT = 1024;

    // OpenGL handles
    static int mainShader;
    static int depthShader;

    static int floorVAO, cubeVAO;

    static int shadowFBO;         // Framebuffer pre depth pass
    static int shadowMapTexture;  // Depth textúra

    // Matice
    static Matrix4f projection;
    static Matrix4f view;
    static Matrix4f lightSpaceMatrix; // Matica pre pohľad svetla

    public static void main(String[] args) {
        if (!glfwInit()) throw new RuntimeException("GLFW init failed");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        long window = glfwCreateWindow(WIDTH, HEIGHT, "Shadow Mapping Workshop", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Window creation failed");

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();

        // Pohyb kamery len keď je stlačené pravé tlačidlo
        glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {
            if (glfwGetMouseButton(win, GLFW_MOUSE_BUTTON_LEFT) != GLFW_PRESS) {
                firstMouse = true;
                return;
            }
            if (firstMouse) { lastX = (float)xpos; lastY = (float)ypos; firstMouse = false; }
            float dx = (float)xpos - lastX;
            float dy = lastY - (float)ypos;
            lastX = (float)xpos; lastY = (float)ypos;
            camYaw   += dx * 0.3f;
            camPitch  = Math.max(-89f, Math.min(89f, camPitch + dy * 0.3f));
        });

        glfwSetScrollCallback(window, (win, xoff, yoff) -> {
            camDist = Math.max(2f, Math.min(30f, camDist - (float)yoff));
        });

        // ESC zatvára okno
        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                glfwSetWindowShouldClose(win, true);
        });

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        glEnable(GL_DEPTH_TEST);

        // Načítanie shaderov zo súborov v /shaders
        mainShader  = loadShader("shaders/main.vert",  "shaders/main.frag");
        depthShader = loadShader("shaders/depth.vert", "shaders/depth.frag");

        // Geometria
        floorVAO = createCube();
        cubeVAO  = createCube();

        // Matice kamery
        projection = new Matrix4f().perspective(
            (float) Math.toRadians(45.0f), (float) WIDTH / HEIGHT, 0.1f, 100.0f);
        view = new Matrix4f().lookAt(
            new Vector3f(0f, 6f, 10f),
            new Vector3f(0f, 0f,  0f),
            new Vector3f(0f, 1f,  0f));

        // ── [CV1] Vytvor Shadow FBO a depth textúru ───────────────────────────
        // TODO: shadowFBO = glGenFramebuffers();

        // TODO: shadowMapTexture = glGenTextures();
        // TODO: glBindTexture(GL_TEXTURE_2D, shadowMapTexture);
        // TODO: glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT,
        //                    0, GL_DEPTH_COMPONENT, GL_FLOAT, (FloatBuffer) null);
        // TODO: glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // TODO: glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // TODO: glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        // TODO: glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        // TODO: glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO);
        // TODO: glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, shadowMapTexture, 0);
        // TODO: glDrawBuffer(GL_NONE);
        // TODO: glReadBuffer(GL_NONE);
        // TODO: glBindFramebuffer(GL_FRAMEBUFFER, 0);

        // ── [CV1] Vypočítaj lightSpaceMatrix ──────────────────────────────────
        // TODO: Matrix4f lightProjection = new Matrix4f().ortho(-10, 10, -10, 10, 1.0f, 20.0f);
        // TODO: Matrix4f lightView = new Matrix4f().lookAt(LIGHT_POS, new Vector3f(0), new Vector3f(0,1,0));
        // TODO: lightSpaceMatrix = lightProjection.mul(lightView);

        // ── Render loop ───────────────────────────────────────────────────────
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            // ── [CV1] PASS 1 — Depth pass ─────────────────────────────────────
            // TODO: glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
            // TODO: glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO);
            // TODO: glClear(GL_DEPTH_BUFFER_BIT);
            // TODO: glUseProgram(depthShader);
            // TODO: setUniformMat4(depthShader, "lightSpaceMatrix", lightSpaceMatrix);
            // TODO: renderScene(depthShader);
            // TODO: glBindFramebuffer(GL_FRAMEBUFFER, 0);

            // ── PASS 2 — Main pass ────────────────────────────────────────────
            glViewport(0, 0, WIDTH, HEIGHT);
            glClearColor(0.08f, 0.10f, 0.14f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float yawRad   = (float) Math.toRadians(camYaw);
            float pitchRad = (float) Math.toRadians(camPitch);
            Vector3f camPos = new Vector3f(
                    camDist * (float)(Math.cos(pitchRad) * Math.cos(yawRad)),
                    camDist * (float) Math.sin(pitchRad),
                    camDist * (float)(Math.cos(pitchRad) * Math.sin(yawRad))
            );
            view = new Matrix4f().lookAt(camPos, new Vector3f(0f), new Vector3f(0f, 1f, 0f));

            glUseProgram(mainShader);
            setUniformMat4(mainShader, "projection", projection);
            setUniformMat4(mainShader, "view", view);
            setUniformVec3(mainShader, "lightPos",   LIGHT_POS);
            setUniformVec3(mainShader, "viewPos",    camPos);
            setUniformVec3(mainShader, "lightColor", new Vector3f(1f, 1f, 1f));

            // ── [CV2] Predaj lightSpaceMatrix a shadowMap do main shadera ─────
            // TODO: setUniformMat4(mainShader, "lightSpaceMatrix", lightSpaceMatrix);
            // TODO: glActiveTexture(GL_TEXTURE0);
            // TODO: glBindTexture(GL_TEXTURE_2D, shadowMapTexture);
            // TODO: setUniformInt(mainShader, "shadowMap", 0);

            renderScene(mainShader);

            glfwSwapBuffers(window);
        }

        glfwTerminate();

    }

    // ── Vykresli scénu (volá sa pre oba prechody) ─────────────────────────────
    static void renderScene(int shader) {
        // Podlaha
        Matrix4f model = new Matrix4f().translate(0f, -0.5f, 0f).scale(10f, 0.2f, 10f);
        setUniformMat4(shader, "model", model);
        setUniformVec3(shader, "objectColor", new Vector3f(0.4f, 0.5f, 0.6f));
        glBindVertexArray(floorVAO);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        // Kocka 1
        model = new Matrix4f().translate(0f, 0.5f, 0f);
        setUniformMat4(shader, "model", model);
        setUniformVec3(shader, "objectColor", new Vector3f(0.7f, 0.3f, 0.2f));
        glBindVertexArray(cubeVAO);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        // Kocka 2
        model = new Matrix4f().translate(2f, 0.25f, -1f).scale(0.5f, 0.5f, 0.5f);
        setUniformMat4(shader, "model", model);
        setUniformVec3(shader, "objectColor", new Vector3f(0.2f, 0.6f, 0.3f));
        glBindVertexArray(cubeVAO);
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    // ── Kocka (36 vertexov: pozícia + normála) ────────────────────────────────
    static int createCube() {
        float[] v = {
            -0.5f,-0.5f,-0.5f, 0f, 0f,-1f,  0.5f,-0.5f,-0.5f, 0f, 0f,-1f,  0.5f, 0.5f,-0.5f, 0f, 0f,-1f,
             0.5f, 0.5f,-0.5f, 0f, 0f,-1f, -0.5f, 0.5f,-0.5f, 0f, 0f,-1f, -0.5f,-0.5f,-0.5f, 0f, 0f,-1f,
            -0.5f,-0.5f, 0.5f, 0f, 0f, 1f,  0.5f,-0.5f, 0.5f, 0f, 0f, 1f,  0.5f, 0.5f, 0.5f, 0f, 0f, 1f,
             0.5f, 0.5f, 0.5f, 0f, 0f, 1f, -0.5f, 0.5f, 0.5f, 0f, 0f, 1f, -0.5f,-0.5f, 0.5f, 0f, 0f, 1f,
            -0.5f, 0.5f, 0.5f,-1f, 0f, 0f, -0.5f, 0.5f,-0.5f,-1f, 0f, 0f, -0.5f,-0.5f,-0.5f,-1f, 0f, 0f,
            -0.5f,-0.5f,-0.5f,-1f, 0f, 0f, -0.5f,-0.5f, 0.5f,-1f, 0f, 0f, -0.5f, 0.5f, 0.5f,-1f, 0f, 0f,
             0.5f, 0.5f, 0.5f, 1f, 0f, 0f,  0.5f, 0.5f,-0.5f, 1f, 0f, 0f,  0.5f,-0.5f,-0.5f, 1f, 0f, 0f,
             0.5f,-0.5f,-0.5f, 1f, 0f, 0f,  0.5f,-0.5f, 0.5f, 1f, 0f, 0f,  0.5f, 0.5f, 0.5f, 1f, 0f, 0f,
            -0.5f,-0.5f,-0.5f, 0f,-1f, 0f,  0.5f,-0.5f,-0.5f, 0f,-1f, 0f,  0.5f,-0.5f, 0.5f, 0f,-1f, 0f,
             0.5f,-0.5f, 0.5f, 0f,-1f, 0f, -0.5f,-0.5f, 0.5f, 0f,-1f, 0f, -0.5f,-0.5f,-0.5f, 0f,-1f, 0f,
            -0.5f, 0.5f,-0.5f, 0f, 1f, 0f,  0.5f, 0.5f,-0.5f, 0f, 1f, 0f,  0.5f, 0.5f, 0.5f, 0f, 1f, 0f,
             0.5f, 0.5f, 0.5f, 0f, 1f, 0f, -0.5f, 0.5f, 0.5f, 0f, 1f, 0f, -0.5f, 0.5f,-0.5f, 0f, 1f, 0f,
        };
        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buf = BufferUtils.createFloatBuffer(v.length);
        buf.put(v).flip();
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
        glBindVertexArray(0);
        return vao;
    }

    // ── Shader loader — číta súbory z disku ──────────────────────────────────
    static int loadShader(String vertPath, String fragPath) {
        String vertSrc = readFile(vertPath);
        String fragSrc = readFile(fragPath);

        int vert = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vert, vertSrc);
        glCompileShader(vert);
        if (glGetShaderi(vert, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Vertex shader error [" + vertPath + "]:\n" + glGetShaderInfoLog(vert));

        int frag = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(frag, fragSrc);
        glCompileShader(frag);
        if (glGetShaderi(frag, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Fragment shader error [" + fragPath + "]:\n" + glGetShaderInfoLog(frag));

        int prog = glCreateProgram();
        glAttachShader(prog, vert);
        glAttachShader(prog, frag);
        glLinkProgram(prog);
        if (glGetProgrami(prog, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Shader link error:\n" + glGetProgramInfoLog(prog));

        glDeleteShader(vert);
        glDeleteShader(frag);
        return prog;
    }

    /**
     * Číta shader súbor z disku.
     * Cesta je relatívna k pracovnému adresáru projektu.
     * V IntelliJ IDEA je pracovný adresár koreň projektu (kde je .iml súbor).
     * Priečinok /shaders/ musí byť priamo v koreni projektu.
     */
    static String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(
                "Shader súbor sa nenašiel: " + path +
                "\nUisti sa, že priečinok 'shaders/' je v koreni projektu: " +
                System.getProperty("user.dir"), e);
        }
    }

    // ── Uniform helpers ───────────────────────────────────────────────────────
    static void setUniformMat4(int prog, String name, Matrix4f mat) {
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        mat.get(buf);
        glUniformMatrix4fv(glGetUniformLocation(prog, name), false, buf);
    }

    static void setUniformVec3(int prog, String name, Vector3f v) {
        glUniform3f(glGetUniformLocation(prog, name), v.x, v.y, v.z);
    }

    static void setUniformInt(int prog, String name, int val) {
        glUniform1i(glGetUniformLocation(prog, name), val);
    }
}


