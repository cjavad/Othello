package othello.faskinen;

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;

import othello.faskinen.x11.X11;

public class X11Window extends Window {
	MemoryAddress display;
	MemoryAddress rootWindow;
	MemoryAddress context;
	long window;
	int screen;

	public X11Window() {
		this.display = X11.XOpenDisplay(Lib.NULLPTR);
		this.screen = X11.XDefaultScreen(this.display);
		this.rootWindow = X11.XRootWindow(this.display, this.screen);
		this.window = X11.XCreateSimpleWindow(
			this.display,
			this.rootWindow,
			0,   // x
			0,   // y
			640, // width
			480, // height
			0,   // border width
			0,   // border
			0    // background
		);

		MemoryAddress windowName = Lib.javaToStr("Othello").address();
		X11.XStoreName(this.display, this.window, windowName);

		MemorySegment settings = MemorySession.global().allocate(32);
		settings.set(ValueLayout.JAVA_INT, 0, X11.GLX_RGBA);
		settings.set(ValueLayout.JAVA_INT, 4, X11.GLX_DEPTH_SIZE);
		settings.set(ValueLayout.JAVA_INT, 8, 24);
		settings.set(ValueLayout.JAVA_INT, 12, X11.GLX_DOUBLEBUFFER);
		settings.set(ValueLayout.JAVA_INT, 16, X11.None);

		MemoryAddress visual = X11.glXChooseVisual(this.display, this.screen, settings.address());

		this.context = X11.glXCreateContext(this.display, visual, Lib.NULLPTR, 1);

		X11.XFree(settings.address());
	}

	public void show() {
		X11.XSelectInput(this.display, this.window, X11.ExposureMask | X11.KeyPressMask);
		X11.XMapWindow(this.display, this.window);
	}

	public void hide() {
	}

	public void pollEvents() {
		MemorySegment event = MemorySession.global().allocate(192);
		X11.XNextEvent(this.display, event.address());
	}

	public void swapBuffers() {
		X11.glXSwapBuffers(this.display, this.window);
	}

	public void makeContextCurrent() {
		X11.glXMakeCurrent(this.display, this.window, this.context);
	}

	public void destroy() {
		X11.XCloseDisplay(this.display);
	}
}
