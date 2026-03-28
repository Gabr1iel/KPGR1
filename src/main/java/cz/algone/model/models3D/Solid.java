package cz.algone.model.models3D.wiredSolids;

import cz.algone.common.enumAlias.ShaderMode;
import cz.algone.objectData.Part;
import cz.algone.objectData.Vertex;
import cz.algone.shader.Shader;
import cz.algone.shader.ShaderConstant;
import cz.algone.shader.ShaderInterpolated;
import cz.algone.shader.ShaderTexture;
import cz.algone.transforms.Col;
import cz.algone.transforms.Mat4;
import cz.algone.transforms.Mat4Identity;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

import java.util.ArrayList;

public abstract class Solid {
    protected ColorPair highlightColor = ColorUtils.DEFAULT_HIGHLIGHT_COLOR;
    protected boolean selected = false;
    protected ArrayList<Vertex> vb = new ArrayList<>();
    protected ArrayList<Integer> ib = new ArrayList<>();
    protected ArrayList<Part> pb = new ArrayList<>();
    protected Shader shader;
    protected Mat4 model = new Mat4Identity();
    protected ColorPair color;
    protected Vec3D position = new Vec3D(0, 0, 0);
    protected Vec3D pivot = new Vec3D(0.5, 0.5, 0.5);
    protected double angleX = 0;
    protected double angleY = 0;
    protected double angleZ = 0;
    protected double scale = 1.0;

    // Shader mode state
    private ShaderMode shaderMode = ShaderMode.CONSTANT;
    private final ShaderInterpolated interpolatedShader = new ShaderInterpolated();
    private ShaderTexture textureShader;

    public ArrayList<Vertex> getVb() { return vb; }
    public ArrayList<Integer> getIb() { return ib; }
    public ArrayList<Part> getPb() { return pb; }
    public Shader getShader() { return shader; }
    public void setShader(Shader shader) { this.shader = shader; }

    // ─── Shader mode ────────────────────────────────────────────────────────────

    public ShaderMode getShaderMode() { return shaderMode; }

    public void setShaderMode(ShaderMode mode) {
        if (mode == ShaderMode.TEXTURED && textureShader == null) {
            textureShader = new ShaderTexture(); // default checkerboard
        }
        this.shaderMode = mode;
        applyShader();
    }

    /** Cycles CONSTANT → INTERPOLATED → TEXTURED → CONSTANT on the active solid. */
    public void cycleShaderMode() {
        ShaderMode[] modes = ShaderMode.values();
        setShaderMode(modes[(shaderMode.ordinal() + 1) % modes.length]);
    }

    public void setTexture(ShaderTexture texture) {
        this.textureShader = texture;
        if (shaderMode == ShaderMode.TEXTURED) applyShader();
    }

    /** Vrací true pokud je nastavena textura s reálným obrázkem (ne procedurální šachovnice). */
    public boolean hasTexture() { return textureShader != null && textureShader.hasImage(); }

    private void applyShader() {
        shader = switch (shaderMode) {
            case CONSTANT     -> new ShaderConstant(color.primary());
            case INTERPOLATED -> interpolatedShader;
            case TEXTURED     -> textureShader;
        };
    }

    // ─── Utility for subclasses ──────────────────────────────────────────────────

    /** Maps absolute xyz position to an RGB color for per-vertex interpolation. */
    protected static Col posColor(double x, double y, double z, double extent) {
        int r = (int) Math.min(255, Math.abs(x) / extent * 255);
        int g = (int) Math.min(255, Math.abs(y) / extent * 255);
        int b = (int) Math.min(255, Math.abs(z) / extent * 255);
        return new Col(r, g, b);
    }

    // ─── Transform ──────────────────────────────────────────────────────────────

    public void resetTransform() {
        position = new Vec3D(0, 0, 0);
        angleX = 0;
        angleY = 0;
        angleZ = 0;
        scale = 1.0;
        model = new Mat4Identity();
    }

    public Mat4 getModel() { return model; }
    public void setModel(Mat4 model) { this.model = model; }

    public ColorPair getColor() { return color; }

    public Vec3D getPosition() { return position; }
    public void setPosition(Vec3D position) { this.position = position; }

    public double getAngleX() { return angleX; }
    public void setAngleX(double angle) { this.angleX = angle; }

    public double getAngleY() { return angleY; }
    public void setAngleY(double angle) { this.angleY = angle; }

    public double getAngleZ() { return angleZ; }
    public void setAngleZ(double angle) { this.angleZ = angle; }

    public double getScale() { return scale; }
    public void setScale(double scale) { this.scale = scale; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public ColorPair getHighlightColor() { return highlightColor; }

    public Vec3D getPivot() { return pivot; }
    public void setPivot(Vec3D pivot) { this.pivot = pivot; }

    protected void addEdge(int a, int b) {
        ib.add(a);
        ib.add(b);
    }

    protected void addTriangle(int a, int b, int c) {
        ib.add(a);
        ib.add(b);
        ib.add(c);
    }
}
