package model.neuronal;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.Clickable2D;
import model.DrawUtils;
import model.Drawable;
import model.physics.CollisionDetection;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2di;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class manages as a graphical user interface
 * the brain
 */
public class BrainManager implements Drawable, Clickable2D {

    /**
     * The brain object what is going to be managed for this instance
     */
    public Brain brain;

    // attributes to draw on screen

    private final Vec2di pos = new Vec2di();

    private final Vec2di size = new Vec2di(800, 600);

    private int backgroundColor = 0xFF8EACAC;

    private final Vec2di paddingLT = new Vec2di(10, 10);

    private final Vec2di paddingRB = new Vec2di(10, 10);

    private final Vec2di realSize = new Vec2di();

    private final Vec2di sizeMiniRect = new Vec2di(15, 15);

    // attributes to manage the user input
    private boolean isSelected = false;

    private boolean isMiniRectSelected = false;

    private boolean isOriConSel = false;

    private boolean isTarConSel = false;

    private Neuron selNeuron = null;

    private Connection selConn = null;

    private Neuron selOriCon = null;

    private Neuron selTarCon = null;

    private final Vec2di mousePos = new Vec2di();

    private final Vec2di offsetBefore = new Vec2di();

    private final Vec2di sizeBefore = new Vec2di();

    private final double INC_WEIGHT_CONNECTION = 0.1;

    /**
     * Constructor
     */
    public BrainManager(Brain brain) {
        this.brain = brain;
        calRealSize();
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInRect(mouseX, mouseY, pos.getX(), pos.getY(), size.getX(), size.getY());
    }

    public Neuron isMouseInsideNeuron(double mouseX, double mouseY) {
        for (Neuron n : brain.getAllNeurons()) {
            if (n.isMouseInside(mouseX, mouseY)) {
                return n;
            }
        }
        return null;
    }

    public boolean isMouseInsideMiniRect(double mouseX, double mouseY) {
        return CollisionDetection.isPointInRect(mouseX, mouseY,
                pos.getX() + (size.getX() - sizeMiniRect.getX()),
                pos.getY() + (size.getY() - sizeMiniRect.getY()),
                sizeMiniRect.getX(),
                sizeMiniRect.getY());
    }

    public void updateUserInput(Input input, float dt) {
        if (input.isButtonDown(MouseButton.PRIMARY)) {
            selNeuron = isMouseInsideNeuron(input.getMouseX(), input.getMouseY());

            if (selNeuron == null) {
                Map.Entry<Neuron, Connection> con;
                for (Neuron n : brain.getAllNeurons()) {
                    con = n.isMouseInsideConnection(input.getMouseX(), input.getMouseY());
                    if (con != null) {
                        selConn = con.getValue();
                        selOriCon = n;
                        selTarCon = con.getKey();
                        if (CollisionDetection.isPointInCircle(input.getMouseX(), input.getMouseY(), selOriCon.getB())) {
                            isOriConSel = true;
                        }
                        if (!isOriConSel && CollisionDetection.isPointInCircle(input.getMouseX(), input.getMouseY(), selOriCon.getB())) {
                            isTarConSel = true;
                        }
                        break;
                    }
                }
            }

            if (isMouseInsideMiniRect(input.getMouseX(), input.getMouseY())) {
                isMiniRectSelected = true;
                mousePos.setX((int)input.getMouseX());
                mousePos.setY((int)input.getMouseY());
                sizeBefore.setX(size.getX());
                sizeBefore.setY(size.getY());
            }

            if (isMouseInside(input.getMouseX(), input.getMouseY()) &&
                    selNeuron == null &&
                    !isMiniRectSelected &&
                    selConn == null) {
                isSelected = true;
                mousePos.setX((int)input.getMouseX());
                mousePos.setY((int)input.getMouseY());
                offsetBefore.setX(pos.getX());
                offsetBefore.setY(pos.getY());
            }
        }

        if (input.isButtonHeld(MouseButton.PRIMARY)) {
            if (selNeuron != null) {
                selNeuron.getB().getPos().setX((float) input.getMouseX());
                selNeuron.getB().getPos().setY((float) input.getMouseY());
                calConnectionsPositions();
            }
            if (isSelected) {
                pos.setX(offsetBefore.getX() + ((int) input.getMouseX() - mousePos.getX()));
                pos.setY(offsetBefore.getY() + ((int) input.getMouseY() - mousePos.getY()));
                calNeuronsPositions();
            }
            if (isMiniRectSelected) {
                size.setX(sizeBefore.getX() + ((int)input.getMouseX() - sizeBefore.getX()));
                size.setY(sizeBefore.getY() + ((int)input.getMouseY() - sizeBefore.getY()));
                calNeuronsPositions();
            }
            if (selConn != null && isOriConSel) {
                selConn.getOri().setX((float) input.getMouseX());
                selConn.getOri().setY((float) input.getMouseY());
            }
            if (selConn != null && isTarConSel) {
                selConn.getTar().setX((float) input.getMouseX());
                selConn.getTar().setY((float) input.getMouseY());
            }
        }

        if (selConn != null) {
            selConn.addToWeight((input.getScroll() / 100.0) * dt);

            if (input.isKeyHeld(KeyCode.PLUS)) {
                selConn.addToWeight(INC_WEIGHT_CONNECTION * dt);
            }
            if (input.isKeyHeld(KeyCode.MINUS)) {
                selConn.addToWeight(-INC_WEIGHT_CONNECTION * dt);
            }
            if (input.isKeyHeld(KeyCode.X)) {
                selOriCon.getChildren().remove(selTarCon, selConn);
            }

            if (selConn.getWeight() > 1) {
                selConn.setWeight(1);
            }
            if (selConn.getWeight() < -1) {
                selConn.setWeight(-1);
            }
        }

        if (input.isButtonUp(MouseButton.PRIMARY)) {
            selNeuron = null;
            selConn = null;
            selOriCon = null;
            selTarCon = null;

            isSelected = false;
            isMiniRectSelected = false;

            isOriConSel = false;
            isTarConSel = false;

            mousePos.setX(0);
            mousePos.setY(0);

            offsetBefore.setX(0);
            offsetBefore.setY(0);
            sizeBefore.setX(0);
            sizeBefore.setY(0);
        }
    }

    // Cal position methods

    /**
     * For each neuron, update the position of the connection
     */
    public void calConnectionsPositions() {
        for (Neuron n : brain.getAllNeurons()) {
            n.calConnectionPos();
        }
    }

    public void calNeuronsPositions() {
        calRealSize();

        // todo hay problemas con el calculo del ancho de la seccion por ser una division entera, hay que acarrear el error
        int x = pos.getX() + paddingLT.getX();

        int sectionW = realSize.getX();
        if (brain.getNumLayers() >= 1) {
            sectionW /= brain.getNumLayers();
        }

        x += sectionW / 2;
        for (ArrayList<Neuron> layer : brain.getLayers()) {
            int y = pos.getY() + paddingLT.getY();

            int sectionH = realSize.getY();
            if (layer.size() >= 1) {
                sectionH /= layer.size();
            }

            y += sectionH / 2;
            for (Neuron n : layer) {
                n.getB().getPos().setX(x);
                n.getB().getPos().setY(y);
                y += sectionH;
            }
            x += sectionW;
        }

        calConnectionsPositions();
    }

    /**
     * Cal the real size of the square to paint the brain
     */
    public void calRealSize() {
        realSize.setX(size.getX() - paddingLT.getX() - paddingRB.getX());
        realSize.setY(size.getY() - paddingLT.getX() - paddingRB.getX());
    }

    // drawing methods

    private void drawSections(Renderer r) {
        int sectionW = realSize.getX() / brain.getNumLayers();
        int x = pos.getX() + paddingLT.getX();
        int y = pos.getY() + paddingLT.getY();
        for (int i = 0; i < brain.getNumLayers(); i++) {
            r.drawRect(x, y, sectionW, realSize.getY(), HexColors.BLACK);
            String layerName = "Layer " + i;
            if (i == 0) {
                layerName = "Input layer";
            }
            if (i == brain.getNumLayers() - 1) {
                layerName = "Output layer";
            }
            r.drawText(layerName, x + 5, y + 5, HexColors.BLACK);
            x += sectionW;
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        r.drawFillRect(pos.getX(), pos.getY(), size.getX(), size.getY(), backgroundColor);
        drawSections(r);
        for (Neuron n : brain.getAllNeurons().stream()
                .sorted(Comparator.comparingInt(Neuron::getLayer)).collect(Collectors.toList())) {
            n.drawYourSelf(r);
        }
        if (selConn != null) {
            DrawUtils.drawSquare(r, selConn.getXp(), selConn.getYp(), HexColors.YELLOW);
        }
        if (selNeuron != null) {
            r.drawCircle(
                    (int) selNeuron.getB().getPos().getX(),
                    (int) selNeuron.getB().getPos().getY(),
                    selNeuron.getB().getR(),
                    HexColors.YELLOW);
        }
    }

    // Getters and Setters

    public void setDrawingParameters(Vec2di size, Vec2di offset, Vec2di paddingLT, Vec2di paddingRB) {
        // Copy the values
        this.size.setX(size.getX());
        this.size.setY(size.getY());

        pos.setX(offset.getX());
        pos.setY(offset.getY());

        this.paddingLT.setX(paddingLT.getX());
        this.paddingLT.setY(paddingLT.getY());

        this.paddingRB.setX(paddingRB.getX());
        this.paddingRB.setY(paddingRB.getY());

        calRealSize();
    }

    public Brain getBrain() {
        return brain;
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }

}
