/**
 * Doc for Cystem
 */
package ca.unbc.kranz.allan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

/**
 * <p>
 * The Cystem class is a replacement for System in a Java program. It cannot be
 * instantiated
 * </p>
 * 
 * <p>
 * The {@code Cystem} class provides a standard input stream, a standard output
 * stream, and a console for simple presentation control of text and screen
 * colours. Using the {@code Cystem} class closely mimics using {@code java.lang.System}.
 * It does not provide an error stream or  access to externally defined properties and 
 * environment variables; a means of loading files and libraries; and a utility method 
 * for quickly copying a portion of an array.
 * </p>
 * 
 * <p>
 * The output stream {@code out} can be used to print to the Cystem console by
 * simply by calling {@code Cystem.out.println("Message); } and all the types
 * supported by {@code System.out } are supported by {@code Cystem.out}.
 * </p>
 * 
 * <p>
 * {@code Cystem.in} is meant to used exactly like {@code System.in}, typically
 * use is with java.util.Scanner as follows;
 * </p>
 * 
 * {@code Scanner cin = new Scanner(Cystem.in);}<br>
 * {@code String input = cin.nextLine();}
 * 
 * <p>
 * To control the console colors
 * </p>
 * 
 * {@code Cystem.console.setTextColor(Color c);}<br>
 * {@code Cystem.console.setScreenColor(Color c);}
 * 
 * <p>
 * No attempt is made to check for colours that will not be visible. The default
 * color is bright green for text and black for the screen.
 * </p>
 * 
 * <p> Typical use looks like </p>
 * {@code Cystem.console.setScreenColor(Color.DARK_GRAY);}<br>
 * {@code Cystem.console.setTextColor(Color.ORANGE);}<br>
 * {@code Cystem.console.makeVisible();}<br>
 * {@code Cystem.out.println("Starting Cystem console...");}<br>
 * {@code Scanner cin = new Scanner(Cystem.in);}<br>
 * {@code Cystem.out.println("Enter your age:");}<br>
 * {@code int x = cin.nextInt();}<br>
 * {@code Cystem.out.printf("%s %d\n","Your age is ", x);	}<br>
 * {@code cin.nextLine();}<br>
 * {@code Cystem.out.print("Enter anything...");}<br>
 * {@code String s = cin.nextLine();}<br>
 * {@code Cystem.console.makeInvisible();}<br>
 * {@code Cystem.console.shutdown();}<br>
 * 
 * 
 * @author Allan Kranz (allan.kranz@unbc.ca)
 *
 */
public final class Cystem {

	static private JFrame frame = new JFrame();
	static private JTextArea textArea = new JTextArea();

	// A private constructor
	private Cystem() {

	}

	/**
	 * Offers simple control of text and screen colours.
	 */
	public final static ConsoleGUI console = new ConsoleGUI();

	/**
	 * An input stream that mimics the "standard" input stream.  This stream is already open 
	 * and ready to supply input data. 
	 */
	public final static ConsoleGUIIn in = new ConsoleGUIIn();

	/**
	 * An output stream that mimics the "standard" output stream.This stream is already open 
	 * and ready to accept output data. 
	 */
	public final static ConsoleGUIOut out = new ConsoleGUIOut();

	/**
	 * ConsoleGUIIn provides services as an InputStream that comes form the
	 * console screen.
	 * 
	 * @author Allan Kranz (allan.kranz@unbc.ca)
	 */
	static class ConsoleGUIIn extends java.io.InputStream implements java.awt.event.KeyListener, java.io.Closeable {

		/**
		 * Constructs a default input stream connected to the console.
		 */
		private ConsoleGUIIn() {

		}

		private LinkedList<Byte> byteQueue = new LinkedList<Byte>();
		private StringBuilder sb = new StringBuilder();

		/**
		 * This does nothing because we are going to control this stuff
		 * eternally with other methods.
		 */
		@Override
		public void close() {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}

		/**
		 * This is where we stuff the buffer for the user read operation. If we
		 * see an enter key pressed in the JTextArea we grab what has been
		 * typed, load the bytes into a List.
		 * 
		 * @param data
		 *            The bytes that will be returned to the stream.
		 */
		public synchronized void setData(byte[] data) {
			byteQueue.clear();

			for (int i = 0; i < data.length; i++) {
				byteQueue.addLast(new Byte(data[i]));
			}
			// TODO Rewrite this section to use String.toBytes
			String lineSeperator = System.getProperty("line.separator");
			for (int i = 0; i < lineSeperator.length(); i++) {
				byteQueue.addLast(new Byte((byte) lineSeperator.charAt(i)));
			}

			byteQueue.addLast(new Byte((byte) -1));

			notifyAll();
		}

		/**
		 * This method reads an unsigned byte from the input stream and returns
		 * it as an int in the range of 0-255. This method also will return -1
		 * if the end of the stream has been reached.
		 * <p>
		 * This method will block until a byte can be read.
		 *
		 * @return The byte read or -1 if end of stream
		 *
		 * @exception IOException
		 *                If an error occurs
		 */
		@Override
		public synchronized int read() throws IOException {

			while (byteQueue.size() == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return (int) (byteQueue.removeFirst().byteValue());
		}

		/**
		 * This is where we detect the enter key and load the buffer with any
		 * characters that are typed on the console screen.
		 * 
		 * @param e
		 *            The KeyEvent that just happened
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				setData(sb.toString().getBytes());
				sb.setLength(0);
			} else {
				sb.append(e.getKeyChar());
			}
		}

		/**
		 * Does nothing
		 */
		@Override
		public void keyReleased(KeyEvent e) {

		}

		/**
		 * Does nothing
		 */
		@Override
		public void keyTyped(KeyEvent e) {

		}
	}

	/**
	 * ConsoleGUIOut is a PrintStream that supports the full range of
	 * PrintStream capabilities but onto the console screen rather than standard
	 * out.
	 * 
	 * @author Allan Kranz (allan.kranz@unbc.ca)
	 */
	static class ConsoleGUIOut extends java.io.PrintStream {

		/**
		 * Interception of the write method allows sending the stream to the
		 * console screen.
		 * 
		 * @author Allan Kranz (allan.kranz@unbc.ca)
		 */
		private static OutputStream outputStream = new OutputStream() {

			/**
			 * This method writes <code>len</code> bytes from the specified
			 * array <code>b</code> starting at index <code>off</code> into the
			 * array.
			 * <p>
			 * This method in this class calls the single byte
			 * <code>write()</code> method in a loop until all bytes have been
			 * written. Subclasses should override this method if possible in
			 * order to provide a more efficient implementation.
			 *
			 * @param b
			 *            The array of bytes to write from
			 * @param off
			 *            The index into the array to start writing from
			 * @param len
			 *            The number of bytes to write
			 * 
			 * @exception IOException
			 *                If an error occurs
			 * 
			 *                This is where the interception of the byte stream
			 *                occurs, gets converted back to a String and
			 *                displayed on the console screen.
			 */
			@Override
			public void write(byte[] b, int off, int len) throws IOException {

				if (textArea != null) {
					textArea.append(new String(Arrays.copyOfRange(b, off, len)));
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
			}

			/**
			 * This method writes a single byte to the output stream. The byte
			 * written is the low eight bits of the <code>int</code> passed and
			 * a argument.
			 * <p>
			 * Subclasses must provide an implementation of this abstract method
			 *
			 * @param b
			 *            The byte to be written to the output stream, passed as
			 *            the low eight bits of an <code>int</code>
			 *
			 * @exception IOException
			 *                If an error occurs
			 * 
			 *                This method is not required.
			 * 
			 * @see public void write(byte[] b, int off, int len) throws
			 *      IOException
			 */
			@Override
			public void write(int b) throws IOException {
				byte[] bAsArray = new byte[1];
				bAsArray[0] = (byte)b;
				write(bAsArray,0,1);
			}
		};

		/**
		 * 
		 */
		private ConsoleGUIOut() {
			super(outputStream);
		}
	}

	/**
	 * Provides simple control over text and screen colors.
	 * 
	 * @author Allan Kranz (allan.kranz@unbc.ca)
	 */
	static class ConsoleGUI {

		/**
		 * The {@code java.awt.Color} the console text will be drawn with.
		 */
		private Color textColor = new Color(158, 255, 163);

		/**
		 * The {@code java.awt.Color} the console background will be drawn with.
		 */
		private Color screenColor = new Color(0, 0, 0);

		/**
		 * The {@code java.awt.Color} the console cursor will be drawn with.
		 */
		private Color caretColor = new Color(255, 255, 255);

		/**
		 * This just makes the console visible.
		 */
		public void makeVisible() {
			frame.setVisible(true);
		}

		/**
		 * This just makes the console invisible.
		 */
		public void makeInvisible() {
			frame.setVisible(false);
		}

		/**
		 * 
		 */
		public void startup() {
			// frame.setVisible(true);
		}

		/**
		 * Call this when the console is no longer required. This will close the
		 * console and relaese the resources.
		 */
		public void shutdown() {
			frame.setVisible(false);
			frame.dispose();
		}

		/**
		 * Sets the cursor color for the console screen. Take care not to set
		 * the cursor color and screen color to the same color if you want to
		 * see the cursor.
		 * 
		 * @param cursorColor
		 *            The {@code java.awt.Color} for the text.
		 */
		public void setCursorColor(Color cursorColor) {
			this.caretColor = cursorColor;
		}

		/**
		 * Sets the foreground colour for the console screen. Take care not to
		 * set the text colour and screen colour to the same colour.
		 * 
		 * @param textColor
		 *            The {@code java.awt.Color} for the text.
		 */
		public void setTextColor(Color textColor) {
			this.textColor = textColor;
		}

		/**
		 * Sets the background colour for the console screen. Take care not to
		 * set the text colour and screen colour to the same colour.
		 * 
		 * @param screenColor
		 *            The {@code java.awt.Color} for the screen background.
		 */
		public void setScreenColor(Color screenColor) {
			this.screenColor = screenColor;
		}

		/**
		 * This runs the GUI in the ED thread
		 */
		private ConsoleGUI() {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					createAndShowGUI();
				}
			});
		}

		/**
		 * This creates the GUI
		 */
		private void createAndShowGUI() {
			
			frame.dispose();
			frame.setUndecorated(true);

			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			frame.setLayout(new BorderLayout());

			//textArea = new JTextArea();

			textArea.setBackground(screenColor);
			textArea.setForeground(textColor);
			textArea.setFont(new Font("COURIER", Font.PLAIN, 16));
			textArea.setCaretColor(caretColor);
			textArea.addKeyListener(in);

			JScrollPane scrollPane = new JScrollPane(textArea);

			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			panel.add(scrollPane, BorderLayout.CENTER);

			frame.getContentPane().add(panel, BorderLayout.CENTER);

			frame.setMinimumSize(new Dimension(700, 800));
			frame.pack();
			frame.setLocationRelativeTo(null);
			//frame.setVisible(true);
		}
	}
}
