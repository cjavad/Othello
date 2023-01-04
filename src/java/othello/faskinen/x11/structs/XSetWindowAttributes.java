package othello.faskinen.x11.structs;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemoryLayout;
import java.lang.invoke.VarHandle;

import othello.faskinen.Lib;

public class XSetWindowAttributes {
	public static final GroupLayout layout = MemoryLayout.structLayout(
		Lib.C_UINT64_T("background_pixmap"),
		Lib.C_UINT64_T("background_pixel"),
		Lib.C_UINT64_T("border_pixmap"),
		Lib.C_UINT64_T("border_pixel"),
		Lib.C_INT32_T("bit_gravity"),
		Lib.C_INT32_T("win_gravity"),
		Lib.C_INT32_T("backing_store"),
		Lib.C_UINT64_T("backing_planes"),
		Lib.C_UINT64_T("backing_pixel"),
		Lib.C_BOOL_T("save_under"),
		Lib.C_UINT64_T("event_mask"),
		Lib.C_UINT64_T("do_not_propagate_mask"),
		Lib.C_BOOL_T("override_redirect"),
		Lib.C_UINT64_T("colormap"),
		Lib.C_UINT64_T("cursor")
	);

	private static final VarHandle
		background_pixmap = Lib.getStructField(layout, "background_pixmap"),
		background_pixel = Lib.getStructField(layout, "background_pixel"),
		border_pixmap = Lib.getStructField(layout, "border_pixmap"),
		border_pixel = Lib.getStructField(layout, "border_pixel"),
		bit_gravity = Lib.getStructField(layout, "bit_gravity"),
		win_gravity = Lib.getStructField(layout, "win_gravity"),
		backing_store = Lib.getStructField(layout, "backing_store"),
		backing_planes = Lib.getStructField(layout, "backing_planes"),
		backing_pixel = Lib.getStructField(layout, "backing_pixel"),
		save_under = Lib.getStructField(layout, "save_under"),
		event_mask = Lib.getStructField(layout, "event_mask"),
		do_not_propagate_mask = Lib.getStructField(layout, "do_not_propagate_mask"),
		override_redirect = Lib.getStructField(layout, "override_redirect"),
		colormap = Lib.getStructField(layout, "colormap"),
		cursor = Lib.getStructField(layout, "cursor");

	public static final long SIZE = layout.byteSize();
	public static long sizeof() { return SIZE; }

	public static long get_background_pixmap(MemoryAddress addr) { return (long) background_pixmap.get(addr); }
	public static void set_background_pixmap(MemoryAddress addr, long value) { background_pixmap.set(addr, value); }

	public static long get_background_pixel(MemoryAddress addr) { return (long) background_pixel.get(addr); }
	public static void set_background_pixel(MemoryAddress addr, long value) { background_pixel.set(addr, value); }

	public static long get_border_pixmap(MemoryAddress addr) { return (long) border_pixmap.get(addr); }
	public static void set_border_pixmap(MemoryAddress addr, long value) { border_pixmap.set(addr, value); }

	public static long get_border_pixel(MemoryAddress addr) { return (long) border_pixel.get(addr); }
	public static void set_border_pixel(MemoryAddress addr, long value) { border_pixel.set(addr, value); }

	public static int get_bit_gravity(MemoryAddress addr) { return (int) bit_gravity.get(addr); }
	public static void set_bit_gravity(MemoryAddress addr, int value) { bit_gravity.set(addr, value); }

	public static int get_win_gravity(MemoryAddress addr) { return (int) win_gravity.get(addr); }
	public static void set_win_gravity(MemoryAddress addr, int value) { win_gravity.set(addr, value); }

	public static int get_backing_store(MemoryAddress addr) { return (int) backing_store.get(addr); }
	public static void set_backing_store(MemoryAddress addr, int value) { backing_store.set(addr, value); }

	public static long get_backing_planes(MemoryAddress addr) { return (long) backing_planes.get(addr); }
	public static void set_backing_planes(MemoryAddress addr, long value) { backing_planes.set(addr, value); }

	public static long get_backing_pixel(MemoryAddress addr) { return (long) backing_pixel.get(addr); }
	public static void set_backing_pixel(MemoryAddress addr, long value) { backing_pixel.set(addr, value); }

	public static boolean get_save_under(MemoryAddress addr) { return (boolean) save_under.get(addr); }
	public static void set_save_under(MemoryAddress addr, boolean value) { save_under.set(addr, value); }

	public static long get_event_mask(MemoryAddress addr) { return (long) event_mask.get(addr); }
	public static void set_event_mask(MemoryAddress addr, long value) { event_mask.set(addr, value); }

	public static long get_do_not_propagate_mask(MemoryAddress addr) { return (long) do_not_propagate_mask.get(addr); }
	public static void set_do_not_propagate_mask(MemoryAddress addr, long value) { do_not_propagate_mask.set(addr, value); }

	public static boolean get_override_redirect(MemoryAddress addr) { return (boolean) override_redirect.get(addr); }
	public static void set_override_redirect(MemoryAddress addr, boolean value) { override_redirect.set(addr, value); }

	public static long get_colormap(MemoryAddress addr) { return (long) colormap.get(addr); }
	public static void set_colormap(MemoryAddress addr, long value) { colormap.set(addr, value); }

	public static long get_cursor(MemoryAddress addr) { return (long) cursor.get(addr); }
	public static void set_cursor(MemoryAddress addr, long value) { cursor.set(addr, value); }
}
