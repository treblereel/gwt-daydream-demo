package org.treblereel.gwt.ar.client.api;


import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel on 5/15/18.
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Window")
public class WindowVR extends elemental2.dom.Window {

    /**
     * The onvrdisplaypresentchange property of the Window interface represents an event handler that will run when the presenting state of a VR display changes — i.e. goes from presenting to not presenting, or vice versa (when the vrdisplaypresentchange event fires).
     * <p>
     * The event object is of type VRDisplayEvent.
     */
    public VRDisplayEvent onvrdisplaypresentchange;

    /**
     * The vrdisplayfocus event of the WebVR API is fired when presentation to a VR display has resumed after being blurred.
     */
    public VRDisplayEvent vrdisplayfocus;

    /**
     * The vrdisplaydisconnect event of the WebVR API is fired when a compatible VR display is disconnected from the computer.
     */
    public VRDisplayEvent vrdisplaydisconnect;

    /**
     * The vrdisplaydeactivate event of the WebVR API is fired when a VR display can no longer be presented to, for example if an HMD has gone into standby or sleep mode due to a period of inactivity.
     */
    public VRDisplayEvent vrdisplaydeactivate;

    /**
     * The vrdisplayconnect event of the WebVR API is fired when a compatible VR display is connected to the computer.
     */
    public VRDisplayEvent vrdisplayconnect;

    /**
     * The vrdisplayblur event of the WebVR API is fired when presentation to a VR display has been paused for some reason by the browser, OS, or VR hardware — for example, while the user is interacting with a system menu or browser, to prevent tracking or loss of experience.
     */
    public VRDisplayEvent vrdisplayblur;

    /**
     * The vrdisplayactivate event of the WebVR API is fired when a VR display is able to be presented to, for example if an HMD has been moved to bring it out of standby, or woken up by being put on.
     */
    public VRDisplayEvent vrdisplayactivate;


    @JsFunction
    public interface VRDisplayEvent {
        void onEvent(elemental2.vr.VRDisplayEvent event);
    }

}
