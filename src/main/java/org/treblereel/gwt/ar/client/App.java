package org.treblereel.gwt.ar.client;

import com.google.gwt.core.client.EntryPoint;
import elemental2.core.Float32Array;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Window;
import jsinterop.base.Js;
import org.treblereel.gwt.ar.client.api.NavigatorVR;
import org.treblereel.gwt.three4g.cameras.PerspectiveCamera;
import org.treblereel.gwt.three4g.core.BufferGeometry;
import org.treblereel.gwt.three4g.core.Geometry;
import org.treblereel.gwt.three4g.core.Object3D;
import org.treblereel.gwt.three4g.core.bufferattributes.Float32BufferAttribute;
import org.treblereel.gwt.three4g.examples.vr.WebVR;
import org.treblereel.gwt.three4g.examples.vr.daydream.DaydreamController;
import org.treblereel.gwt.three4g.geometries.BoxGeometry;
import org.treblereel.gwt.three4g.geometries.IcosahedronGeometry;
import org.treblereel.gwt.three4g.lights.DirectionalLight;
import org.treblereel.gwt.three4g.lights.HemisphereLight;
import org.treblereel.gwt.three4g.lights.Light;
import org.treblereel.gwt.three4g.materials.LineBasicMaterial;
import org.treblereel.gwt.three4g.materials.MeshBasicMaterial;
import org.treblereel.gwt.three4g.materials.MeshLambertMaterial;
import org.treblereel.gwt.three4g.materials.parameters.LineBasicMaterialParameters;
import org.treblereel.gwt.three4g.materials.parameters.MeshBasicMaterialParameters;
import org.treblereel.gwt.three4g.materials.parameters.MeshLambertMaterialParameters;
import org.treblereel.gwt.three4g.math.Color;
import org.treblereel.gwt.three4g.math.Vector3;
import org.treblereel.gwt.three4g.objects.Line;
import org.treblereel.gwt.three4g.objects.Mesh;
import org.treblereel.gwt.three4g.renderers.OnAnimate;
import org.treblereel.gwt.three4g.renderers.WebGLRenderer;
import org.treblereel.gwt.three4g.renderers.parameters.WebGLRendererParameters;
import org.treblereel.gwt.three4g.scenes.Scene;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import static elemental2.dom.DomGlobal.alert;
import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {

    private Logger logger = Logger.getLogger(App.class.getSimpleName());
    private HTMLDivElement container;

    private WebGLRenderer renderer;
    private Scene scene;
    private PerspectiveCamera camera;
    private Mesh room;
    private Random rand = new Random();
    private DaydreamController controller;


    public void onModuleLoad() {
        init();
    }

    private void init() {
        container = (HTMLDivElement) DomGlobal.document.createElement("div");
        document.body.appendChild(container);


        scene = new Scene();
        scene.background = new Color(0x505050);
        camera = new PerspectiveCamera(70, window.innerWidth / window.innerHeight, 0.1f, 10);
        MeshBasicMaterialParameters meshBasicMaterialParameters = new MeshBasicMaterialParameters();
        meshBasicMaterialParameters.color = new Color(0x808080);
        meshBasicMaterialParameters.wireframe = true;

        room = new Mesh(
                new BoxGeometry(6, 6, 6, 8, 8, 8),
                new MeshBasicMaterial(meshBasicMaterialParameters)
        );
        room.position.y = 3;
        scene.add(room);
        scene.add(new HemisphereLight(0x606060, 0x404040));
        Light light = new DirectionalLight(0xffffff);
        light.position.set(1, 1, 1).normalize();
        scene.add(light);
        Geometry geometry = new IcosahedronGeometry(0.08f, 2);


        for (int i = 0; i < 200; i++) {
            MeshLambertMaterialParameters meshLambertMaterial = new MeshLambertMaterialParameters();
            meshLambertMaterial.color = new Color(rand.nextFloat() * 0xffffff);

            Mesh object = new Mesh(geometry, new MeshLambertMaterial(meshLambertMaterial));
            object.position.x = rand.nextFloat() * 4 - 2;
            object.position.y = rand.nextFloat() * 4 - 2;
            object.position.z = rand.nextFloat() * 4 - 2;

            Vector3 velocity = new Vector3();
            velocity.x = rand.nextFloat() * 0.01f - 0.005f;
            velocity.y = rand.nextFloat() * 0.01f - 0.005f;
            velocity.z = rand.nextFloat() * 0.01f - 0.005f;

            object.userData.set("velocity", velocity);
            room.add(object);
        }

        WebGLRendererParameters parameters = new WebGLRendererParameters();
        parameters.alpha = true;

        renderer = new WebGLRenderer(parameters);
        renderer.setSize(window.innerWidth, window.innerHeight);
        renderer.vr.enabled = true;

        alert("is enable ? " + renderer.vr.enabled);

        container.appendChild(renderer.domElement);
        container.appendChild(WebVR.createButton(renderer));

        controller = new DaydreamController();
        controller.position.set(0.3f, 0.75f, 0);
        scene.add(controller);

        LineBasicMaterialParameters lineBasicMaterial = new LineBasicMaterialParameters();
        lineBasicMaterial.linewidth = 2;

        BufferGeometry bufferGeometry = new BufferGeometry();
        bufferGeometry.addAttribute("position", new Float32BufferAttribute(Float32Array.from(new double[]{0, 0, 0, 0, 0, -10}), 3));

        Line controllerHelper = new Line(bufferGeometry, new LineBasicMaterial(lineBasicMaterial));
        controller.add(controllerHelper);

        window.addEventListener("resize", evt -> onWindowResize(), false);

        animate();

    }

    private void onWindowResize() {
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(window.innerWidth, window.innerHeight);
    }

    private void animate() {
        renderer.setAnimationLoop(new OnAnimate() {
            @Override
            public void animate() {
                render();
            }
        });
    }

    private void render() {
        controller.update();
        if (controller.getTouchpadState() == true) {
            Object3D cube = room.children[0];
            room.remove(cube);
            cube.position.copy(controller.position).sub(room.position);

            Vector3 velocity = Js.uncheckedCast(cube.userData.get("velocity"));
            velocity.x = (rand.nextFloat() - 0.5f) * 0.02f;
            velocity.y = (rand.nextFloat() - 0.5f) * 0.02f;
            velocity.z = (rand.nextFloat() * 0.02f - 0.1f);
            velocity.applyQuaternion(controller.quaternion);

            room.add(cube);
        }
        // keep cubes inside room
        float range = 3 - 0.08f;
        for (int i = 0; i < room.children.length; i++) {
            Object3D cube = room.children[i];
            Vector3 velocity = Js.uncheckedCast(cube.userData.get("velocity"));
            cube.position.add(velocity);
            if (cube.position.x < -range || cube.position.x > range) {
                cube.position.x = org.treblereel.gwt.three4g.math.Math.clamp(cube.position.x, -range, range);
                velocity.x = -velocity.x;
            }
            if (cube.position.y < -range) {
                cube.position.y = Math.max(cube.position.y, -range);
                //TODO check, maybe better do not have a refrence
                velocity.x *= 0.9;
                velocity.y = -velocity.y * 0.8f;
                velocity.z *= 0.9f;
            }
            if (cube.position.z < -range || cube.position.z > range) {
                cube.position.z = org.treblereel.gwt.three4g.math.Math.clamp(cube.position.z, -range, range);
                velocity.z = -velocity.z;
            }
            velocity.y -= 0.00098;
        }
        renderer.render(scene, camera);
    }

}
